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

        user.setAdmin(false);
        user.setBanned(false);

        userRepository.create(user);
    }

//    @Override
//    public void deleteById(int id, User user) {
//        checkAdminRole(user);
//        userRepository.deleteById(id);
//    }
//
//    @Override
//    public void deleteByUsername(String username, User user) {
//        checkAdminRole(user);
//        userRepository.deleteByUsername(username);
//    }

    private void checkAdminRole(User user) {
        if (!user.isAdmin()) {
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
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void banUser(int id, boolean banStatus) {
        User user = userRepository.getById(id);

//        if (user.isBanned() == banStatus) {
//            throw new EntityDuplicateException("User status already set to " + (banStatus ? "banned" : "unbanned"));
//        }

        user.setBanned(banStatus);
        userRepository.update(user);
    }
}
