package org.forum.web.forum.repository;

import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;

import java.util.List;

public interface LikePostRepository {

    LikePost get(Post post, User user);

    List<LikePost> getByUserId(int id);

    List<LikePost> getByPostId(int id);

    LikePost getById(int id);

    void create(LikePost likePost);

    void update(LikePost likePost);

    void delete(LikePost likePost);
}
