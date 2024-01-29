package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.UserFilterOptions;
import org.forum.web.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final String AUTHORIZATION_ERROR_MSG = "Access denied. You are not allowed to perform this action.";
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        if (!userDetails.getFirstName().isBlank()) userToUpdate.setFirstName(userDetails.getFirstName());
        if (!userDetails.getLastName().isBlank()) userToUpdate.setLastName(userDetails.getLastName());
        if (!userDetails.getEmail().isBlank()) {
            userToUpdate.setEmail(userDetails.getEmail());
        }

        // Additional check for admin details update
        if(userDetails.getAdminStatus()){
            userToUpdate.setEmail(userDetails.getPhoneNumber().getPhoneNumber());
        }

        userRepository.update(userToUpdate);
    }
}
