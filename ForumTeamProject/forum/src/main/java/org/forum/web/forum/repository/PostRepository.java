package org.forum.web.forum.repository;

import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;

import java.util.List;

public interface PostRepository {

    List<Post>getAll();
    Post getById(int id);
    Post getByTitle(String title);
    void create(Post post);
    void update(Post post);
    void delete(Post post);
    List<User> getLikedBy(int postId);
}
