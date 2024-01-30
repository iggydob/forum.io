package org.forum.web.forum.service.contracts;

import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;

import java.util.List;

public interface LikePostService {
    LikePost get(Post post, User user);

    List<LikePost> getByUserId(int id);

    List<LikePost> getByPostId(int id);

    LikePost getById(int id);

    void create(Post post,User user);

    void update(LikePost likePost,User user);

    void delete(LikePost likePost);
}
