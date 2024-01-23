package org.forum.web.forum.service;

import org.forum.web.forum.models.User;

import java.util.List;

public interface UserService {

    List<User> get();

    User get(int id);

    User get(String username);
}
