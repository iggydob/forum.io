package org.forum.web.forum.repository.contracts;

import org.forum.web.forum.models.Comment;

import java.util.List;

public interface CommentRepository {

    void create(Comment comment);
    void update(Comment comment);
    void delete(int id);
    List<Comment> getAll();
    Comment getById(int id);
    List<Comment> getPostComments(int postId);


}
