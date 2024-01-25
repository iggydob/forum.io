package org.forum.web.forum.repository;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;

import java.util.List;

public interface CommentRepository {

    void create(Comment comment);
    void update(Comment comment);
    void delete(int id);
    List<Comment> getAll();
    Comment getById(int id);


}
