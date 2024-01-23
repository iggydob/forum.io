package org.forum.web.forum.service;

import org.forum.web.forum.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);
}
