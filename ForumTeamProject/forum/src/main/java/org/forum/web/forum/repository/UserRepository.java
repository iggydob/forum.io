package org.forum.web.forum.repository;

import org.forum.web.forum.models.User;
import org.forum.web.forum.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {
    void create(User user);

    void deleteById(int id);

    void deleteByUsername(String username);

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void update(User user);

    List<User> getFiltered(UserFilterOptions userfilterOptions);
}
