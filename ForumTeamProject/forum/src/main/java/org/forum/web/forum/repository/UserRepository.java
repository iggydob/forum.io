package org.forum.web.forum.repository;

import org.forum.web.forum.models.User;
import org.forum.web.forum.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getFiltered(UserFilterOptions userfilterOptions);

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void update(User user);
}
