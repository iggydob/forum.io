package org.forum.web.forum.service.contracts;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.User;

import java.util.List;

public interface CommentService {

    void create(User user, Comment comment);

    void update(User user, Comment comment);

    void delete(User user, int id);

    Comment getById(int commentId);

    List<Comment> getAll();
    long commentLikesCount(int id);
    List<Comment> getPostComments(int postId);

    void likeComment(int commentID, User user);

}
