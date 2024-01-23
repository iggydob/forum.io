package org.forum.web.forum.service;

import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;

import java.util.List;

public interface PoseService {
    List<Post>getAll();
    Post getById(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(Post post, User user);
}
