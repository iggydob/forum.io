package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.contracts.CommentRepository;
import org.forum.web.forum.service.contracts.CommentService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static final String MODIFY_COMMENT_ERROR_MESSAGE = "Only owner or admin can modify a comment.";
    private static final String DUPLICATE_LIKE_COMMENT_ERROR_MESSAGE = "User already liked this comment.";
    private static final String DUPLICATE_DISLIKE_COMMENT_ERROR_MESSAGE = "User already disliked this comment.";
    private static final String DELETE_COMMENT_REACTION_ERROR_MESSAGE = "User has not liked or disliked this comment.";

    private final CommentRepository repository;
    private final AuthorizationHelper authorizationHelper;


    @Autowired
    public CommentServiceImpl(CommentRepository repository, AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;

    }

    @Override
    public void create(User user, Comment comment) {
        authorizationHelper.checkIfBanned(user);
        comment.setCreator(user);
        comment.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        repository.create(comment);
    }

    @Override
    public void update(User user, Comment comment) {
        authorizationHelper.checkAuthor(comment.getCreator(), user);
        authorizationHelper.checkIfBanned(user);
        repository.update(comment);
    }
    @Override
    public void delete(User user, int id) {
        Comment comment = repository.getById(id);
//        authorizationHelper.checkAdmin(user);
//        authorizationHelper.checkAuthor(comment.getCreator(), user);
//        authorizationHelper.checkIfBanned(user);
//        repository.delete(id);

        authorizationHelper.verifyUser(comment.getCreator(), user);
        authorizationHelper.checkIfBanned(user);
        comment.setDeleted(true);

        repository.update(comment);
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
    public long commentLikesCount(int id){
        return repository.commentLikesCount(id);
    }

    @Override
    public long commentDislikesCount(int id){
        return repository.commentDislikesCount(id);
    }

    @Override
    public List<Comment> getPostComments(int postId) {
        return repository.getPostComments(postId);
    }

    @Override
    public void likeComment(int commentID, User user) {
        Comment likeComment = repository.getById(commentID);

        for (Like like : likeComment.getLikedList()){
            if (like.getUser().equals(user)){
                likeComment.getLikedList().remove(like);
                repository.delete(like);
                return;
            }
        }
            Like newLike = new Like(user, likeComment);
            newLike.setLiked(true);
            likeComment.getLikedList().add(newLike);

            repository.update(likeComment);
    }

    @Override
    public void dislikeComment(int commentID, User user) {
        Comment dislikeComment = repository.getById(commentID);

        for (Like dislike : dislikeComment.getDislikedList()){
            if (dislike.getUser().equals(user)){
                dislikeComment.getDislikedList().remove(dislike);
                repository.delete(dislike);
                return;
            }
        }
        Like newDislike = new Like(user, dislikeComment);
        newDislike.setLiked(false);
        dislikeComment.getDislikedList().add(newDislike);

//        repository.update(dislikeComment);
    }

    @Override
    public void deleteReaction(int commentID, User user) {
        Comment commentToDelete = repository.getById(commentID);
        boolean isFound = false;

        Hibernate.initialize(commentToDelete.getLikedList());

        for (Like like : commentToDelete.getLikedList()){
             if (like.getUser().equals(user) && !like.isDeleted()) {
                like.setDeleted(true);
                isFound = true;
                break;
            } else if (like.getUser().equals(user) && like.isDeleted()){
                throw new EntityNotFoundException(DELETE_COMMENT_REACTION_ERROR_MESSAGE);
            }
        }
        if (!isFound) {
            throw new EntityNotFoundException(DELETE_COMMENT_REACTION_ERROR_MESSAGE);
        }

        repository.update(commentToDelete);
    }

}
