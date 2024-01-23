package org.forum.web.forum.repository;

import org.forum.web.forum.models.User;

import java.util.List;

public interface UserRepository {

    List<User> get();
    User get(int id);

    User get (String username);

    void update(User user);
}
