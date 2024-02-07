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
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String AUTHORIZATION_ERROR_MSG = "Access denied. You are not allowed to perform this action.";
    private final UserRepository userRepository;
    private final PhoneNumberService phoneNumberService;
//    private final PasswordEncoderImpl encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PhoneNumberService phoneNumberService) {
        this.userRepository = userRepository;
        this.phoneNumberService = phoneNumberService;
//        this.encoder = encoder;
    }

//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return (UserDetails) getByUsername(username);
//    }
    @Override
    public User create(User user) {
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
//        user.setPassword(encoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());

        userRepository.create(user);
        return user;
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

        if (userDetails.getFirstName() != null) {
            userToUpdate.setFirstName(userDetails.getFirstName());
        }

        if (userDetails.getLastName() != null) {
            userToUpdate.setLastName(userDetails.getLastName());
        }

        if (userDetails.getEmail() != null) {
            userToUpdate.setEmail(userDetails.getEmail());
        }

        if (userToUpdate.getAdminStatus()) {
            if (userDetails.getPhoneNumber() != null) {

                if (userToUpdate.getPhoneNumber() == null) {
                    PhoneNumber newPhoneNumber = new PhoneNumber();
                    newPhoneNumber.setPhoneNumber(userDetails.getPhoneNumber().getPhoneNumber());
                    phoneNumberService.create(newPhoneNumber);
                    userToUpdate.setPhoneNumber(newPhoneNumber);
                } else {

                    userToUpdate.setPhoneNumber(userDetails.getPhoneNumber());
                }
            }

        }

        userRepository.update(userToUpdate);
    }

//    @Autowired
//    private SaveUserUseCase saveUser;
//
//    @Autowired
//    private FindUserByLoginUseCase findUserByLogin;
//
//    /**
//     * Finds a stored user information by login.
//     *
//     * @param login A string representing the user's system login
//     * @return The corresponding user information if successful, or null if it is non-existent.
//     */
//
//    public UserDetails findUserByLogin(String login) {
//        return findUserByLogin.execute(login);
//    }
//
//    /**
//     * Adds a new user to the repository.
//     *
//     * @param userDTO A data transfer object representing a user to add.
//     * @return The saved user if successful,  or null if there is an error.
//     */
//
//    public User addUser(UserDto userDTO) {
//
//        User user = new User(userDTO);
//
//        return saveUser.execute(user);
//    }

}