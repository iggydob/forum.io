package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.CommentRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private static final String MODIFY_COMMENT_ERROR_MESSAGE = "Only owner or admin can modify a comment.";
    private static final String DUPLICATE_LIKE_COMMENT_ERROR_MESSAGE = "User already liked this comment.";
    private static final String DISSLIKE_COMMENT_ERROR_MESSAGE = "User has not liked this comment.";

    private final CommentRepository repository;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public CommentServiceImpl(CommentRepository repository, AuthenticationHelper authenticationHelper) {
        this.repository = repository;
        this.authenticationHelper = authenticationHelper;
    }

    @Override
    public void create(User user, Comment comment) {
        authenticationHelper.checkIfBanned(user);
        comment.setCreator(user);
        comment.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        repository.create(comment);
    }

    @Override
    public void update(User user, Comment comment) {
        authenticationHelper.checkAdmin(user);
        authenticationHelper.checkAuthor(comment.getCreator(), user);
        authenticationHelper.checkIfBanned(user);
        repository.update(comment);
    }
    @Override
    public void delete(User user, int id) {
        Comment comment = repository.getById(id);
        authenticationHelper.checkAdmin(user);
        authenticationHelper.checkAuthor(comment.getCreator(), user);
        authenticationHelper.checkIfBanned(user);
        repository.delete(id);
    }

    @Override
    public Comment getById(int commentId) {
        return repository.getById(commentId);
    }

    @Override
    public List<Comment> getAll() {
        return repository.getAll();
    }

    @Override
    public void likeComment(int commentID, User user) {
        Comment likeComment = repository.getById(commentID);

        Hibernate.initialize(likeComment.getLikedList());

        for (Like like : likeComment.getLikedList()){
            if (like.getUser().equals(user) && !like.isDeleted()){
                throw new EntityDuplicateException(DUPLICATE_LIKE_COMMENT_ERROR_MESSAGE);
            }
        }
        likeComment.getLikedList().add(new Like(user, likeComment));
        repository.update(likeComment);
    }

    @Override
    public void dislikeComment(int commentID, User user) {
        Comment commentToDislike = repository.getById(commentID);

        if (commentToDislike.getLikedList().isEmpty()){
            throw new AuthorizationException(DISSLIKE_COMMENT_ERROR_MESSAGE);
        }
        for (Like like : commentToDislike.getLikedList()){
            if (like.getUser().equals(user) && !like.isDeleted()){
                like.setDeleted(true);
                repository.update(commentToDislike);
                return;
            }
        }
        throw new AuthorizationException(DISSLIKE_COMMENT_ERROR_MESSAGE);
    }

//    public void deleteLike(User user, int id) {
//        Comment comment = repository.getById(id);
//
//        authorization(user, comment);
//        repository.delete(id);
//    }

}
