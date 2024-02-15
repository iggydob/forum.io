package org.forum.web.forum.repository.contracts;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;

import java.util.List;

public interface CommentRepository {

    void create(Comment comment);
    void update(Comment comment);
//    void delete(int id);
    List<Comment> getAll();

    //    public List<Comment> getCommentsSortedByLikes();
    long commentLikesCount(int commentId);
    long commentDislikesCount(int commentId);

    Comment getById(int id);
    void delete(Like like);
    List<Comment> getPostComments(int postId);


}
