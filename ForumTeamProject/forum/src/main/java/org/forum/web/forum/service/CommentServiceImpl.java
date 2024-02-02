package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
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
        boolean isFound = false;

        Hibernate.initialize(likeComment.getLikedList());

        for (Like like : likeComment.getLikedList()){
            if (like.getUser().equals(user) && !like.isDeleted() && like.isLiked()){
                throw new EntityDuplicateException(DUPLICATE_LIKE_COMMENT_ERROR_MESSAGE);
            } else if (like.getUser().equals(user)) {
                if (like.isDeleted()){
                    like.setDeleted(false);
                }
                like.setLiked(true);
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            Like newLike = new Like(user, likeComment);
            newLike.setLiked(true);
            likeComment.getLikedList().add(newLike);
        }

        repository.update(likeComment);
    }

    @Override
    public void dislikeComment(int commentID, User user) {
        Comment commentToDislike = repository.getById(commentID);
        Like newLike = new Like(user,commentToDislike);

        boolean isFound = false;

        Hibernate.initialize(commentToDislike.getLikedList());

        for (Like like : commentToDislike.getLikedList()){
             if (like.getUser().equals(user) && !like.isDeleted() && !like.isLiked()){
                throw new EntityDuplicateException(DUPLICATE_DISLIKE_COMMENT_ERROR_MESSAGE);
            } else if (like.getUser().equals(user)) {
                 if (like.isDeleted()){
                     like.setDeleted(false);
                 }
                 like.setLiked(false);
                 isFound = true;
                 break;
            }
        }

        if (!isFound){
            commentToDislike.getLikedList().add(newLike);
            newLike.setLiked(false);
        }
        repository.update(commentToDislike);
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
