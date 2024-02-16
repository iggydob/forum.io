package org.forum.web.forum.service.contracts;

import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.UserFilterOptions;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getAll();

    User getById(int id, User requester);

    User getByUsername(String username);

    public void changeBanStatus(int id, User userDetails, User requester);

    public void changeBanStatusMvc(int id, boolean status, User requester);

    public void changeAdminStatus(int id, User userDetails, User requester);

    public void changeAdminStatusMvc(int id, boolean status, User requester);

    public void changePassword(int id, User userDetails, User requester);

    public void update(int id, User userDetails, User requester);

    List<User> getFiltered(UserFilterOptions userFilterOptions);
}
