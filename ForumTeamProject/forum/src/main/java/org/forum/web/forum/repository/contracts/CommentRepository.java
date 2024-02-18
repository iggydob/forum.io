package org.forum.web.forum.repository.contracts;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;

import java.util.List;

public interface CommentRepository {

    void create(Comment comment);
    void update(Comment comment);
    List<Comment> getAll();

    long commentLikesCount(int commentId);
//    long commentDislikesCount(int commentId);

    Comment getById(int id);
    void delete(Like like);
    List<Comment> getPostComments(int postId);


}
