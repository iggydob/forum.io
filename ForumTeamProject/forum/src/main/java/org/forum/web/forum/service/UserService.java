package org.forum.web.forum.service;

import org.forum.web.forum.models.User;
import org.forum.web.forum.models.UserFilterOptions;

import java.util.List;

public interface UserService {
    void create(User user);

//    void deleteById(int id, User user);

//    void deleteByUsername(String username, User user);

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    public void banUser(int id, boolean banStatus);

    List<User> getFiltered(UserFilterOptions userFilterOptions);
}
