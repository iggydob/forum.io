package org.forum.web.forum.service;

import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.contracts.CommentRepository;
import org.forum.web.forum.service.contracts.CommentService;
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

}
