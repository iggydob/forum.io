package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private static final String MODIFY_COMMENT_ERROR_MESSAGE = "Only owner or admin can modify a comment.";
    private static final String DUPLICATE_LIKE_COMMENT_ERROR_MESSAGE = "User already liked this comment.";
    private static final String DISSLIKE_COMMENT_ERROR_MESSAGE = "User has not liked this comment.";

    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(Post post, User user, Comment comment) {
        comment.setPost(post);
        comment.setCreator(user);
        repository.create(comment);
    }

    @Override
    public void update(Post post, User user, Comment comment) {
        authorization(user, comment);
        repository.update(comment);
    }




    @Override
    public void delete(User user, Comment comment) {
        authorization(user, comment);
        repository.delete(comment.getId());
    }

    @Override
    public Comment getById(int commentId) {
        return repository.getById(commentId);
    }

    @Override
    public List<Comment> getAll() {
        return repository.getAll();
    }

//    @Override
//    public void likeComment(int commentID, User user) {
//        Comment likeComment = repository.getById(commentID);
//
//        for (Like like : likeComment.getLikedList()){
//            if (like.getUserId() == user.getUserId() && !like.isDeleted()){
//                throw new EntityDuplicateException(DUPLICATE_LIKE_COMMENT_ERROR_MESSAGE);
//            }
//        }
//        likeComment.getLikedList().add(new Like(user.getUserId(), commentID));
//        repository.update(likeComment);
//    }

//    @Override
//    public void dislikeComment(int commentID, User user) {
//        Comment commentToDislike = repository.getById(commentID);
//
//        if (commentToDislike.getLikedList().isEmpty()){
//            throw new AuthorizationException(DISSLIKE_COMMENT_ERROR_MESSAGE);
//        }
//        for (Like like : commentToDislike.getLikedList()){
//            if (like.getUserId() == user.getUserId() && !like.isDeleted()){
//                like.setDeleted(true);
//                repository.update(commentToDislike);
//            }
//        }
//        throw new AuthorizationException(DISSLIKE_COMMENT_ERROR_MESSAGE);
//    }
    private static void authorization(User user, Comment comment) {
        if (comment.getCreator().getUserId() != user.getUserId() && !user.isAdmin()){
            throw new AuthorizationException(MODIFY_COMMENT_ERROR_MESSAGE);
        }
    }
}
