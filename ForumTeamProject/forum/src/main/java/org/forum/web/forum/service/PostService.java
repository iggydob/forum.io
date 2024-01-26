package org.forum.web.forum.service;

import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;

import java.util.List;

public interface PostService {
    List<Post>getAll();
    List<Post> getFiltered(PostFilterOptions postFilterOptions);
    List<Post>getMostRecent();
    List<Post> getMostCommented();
    Post getById(int id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(Post post, User user);
}
