package org.forum.web.forum.repository;

import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;

import java.util.List;

public interface PostRepository {

    List<Post>getByUserId(PostFilterOptions postFilterOptions,int id);
    List<Post> getFiltered(PostFilterOptions postFilterOptions);
    List<Post>getRecent();
    List<Post>getMostCommented();
    Post getById(int id);
    long getPostCount();
    Post getByTitle(String title);
    void create(Post post);
    void update(Post post);
    void delete(Post post);
    List<User> getLikedBy(int postId);
}
