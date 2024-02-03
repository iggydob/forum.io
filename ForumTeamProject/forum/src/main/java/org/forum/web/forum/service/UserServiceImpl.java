package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.PhoneNumber;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.UserFilterOptions;
import org.forum.web.forum.repository.contracts.UserRepository;
import org.forum.web.forum.service.contracts.PhoneNumberService;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String AUTHORIZATION_ERROR_MSG = "Access denied. You are not allowed to perform this action.";
    private final UserRepository userRepository;
    private final PhoneNumberService phoneNumberService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PhoneNumberService phoneNumberService) {
        this.userRepository = userRepository;
        this.phoneNumberService = phoneNumberService;
    }

    @Override
    public void create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        duplicateExists = true;

        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "e-mail", user.getEmail());
        }

        user.setAdminStatus(false);
        user.setBanStatus(false);

        userRepository.create(user);
    }

    private void checkAdminRole(User user) {
        if (!user.getAdminStatus()) {
            throw new AuthorizationException(AUTHORIZATION_ERROR_MSG);
        }
    }

    @Override
    public List<User> getFiltered(UserFilterOptions userFilterOptions) {
        return userRepository.getFiltered(userFilterOptions);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        // TODO: add check here instead of in the controller
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void changeBanStatus(int id, User userDetails) {
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setBanStatus(userDetails.getBanStatus());
        userRepository.update(userToUpdate);
    }

    @Override
    public void changeAdminStatus(int id, User userDetails) {
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setAdminStatus(userDetails.getAdminStatus());

        if (userToUpdate.getPhoneNumber() != null) {
//            PhoneNumber phoneToDelete = userToUpdate.getPhoneNumber();
//            userToUpdate.setEmail(null);
            phoneNumberService.delete(userToUpdate.getPhoneNumber());
        }

        userRepository.update(userToUpdate);
    }

    @Override
    public void changePassword(int id, User userDetails) {
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setPassword(userDetails.getPassword());
        userRepository.update(userToUpdate);
    }

    @Override
    public void update(int id, User userDetails) {
        User userToUpdate = userRepository.getById(id);

        updateFirstName(userDetails, userToUpdate);
        updateLastName(userDetails, userToUpdate);
        updateEmail(userDetails, userToUpdate);
        updatePhoneNumber(userDetails, userToUpdate);

        userRepository.update(userToUpdate);
    }

    private static void updateFirstName(User userDetails, User userToUpdate) {
        if (userDetails.getFirstName() != null) {
            userToUpdate.setFirstName(userDetails.getFirstName());
        }
    }

    private static void updateLastName(User userDetails, User userToUpdate) {
        if (userDetails.getLastName() != null) {
            userToUpdate.setLastName(userDetails.getLastName());
        }
    }

    private void updateEmail(User userDetails, User userToUpdate) {
        if (userDetails.getEmail() != null) {
            boolean duplicateExists = true;

            try {
                userRepository.getByEmail(userDetails.getEmail());
            } catch (EntityNotFoundException e) {
                duplicateExists = false;
            }

            if (duplicateExists) {
                throw new EntityDuplicateException("User", "e-mail", userDetails.getEmail());
            }

            userToUpdate.setEmail(userDetails.getEmail());
        }
    }

    private void updatePhoneNumber(User userDetails, User userToUpdate) {
        if (userToUpdate.getAdminStatus()) {
            if (userDetails.getPhoneNumber() != null) {
                PhoneNumber phoneNumberToUpdate;

                if (userToUpdate.getPhoneNumber() == null) {
                    phoneNumberToUpdate = new PhoneNumber();
                    phoneNumberToUpdate.setUser(userToUpdate);
                    phoneNumberService.create(phoneNumberToUpdate);
                } else {
                    phoneNumberToUpdate = userToUpdate.getPhoneNumber();
                }

                phoneNumberToUpdate.setPhoneNumber(userDetails.getPhoneNumber().getPhoneNumber());
                phoneNumberService.update(phoneNumberToUpdate);
                userToUpdate.setPhoneNumber(phoneNumberToUpdate);
            }
        }
    }
}
