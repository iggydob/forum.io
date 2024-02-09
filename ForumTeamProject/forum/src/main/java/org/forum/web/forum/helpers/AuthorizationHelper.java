package org.forum.web.forum.helpers;

import org.forum.web.forum.exceptions.UnauthorizedOperationException;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {
    private static final String AUTHORIZATION_ERROR = "Access denied! You are not allowed to perform this action!";
    public AuthorizationHelper() {
    }

    public void checkIfBanned(User user) {
        if (user.getBanStatus()) {
            throw new UnauthorizedOperationException("This user is banned!");
        }
    }
    public void checkAuthor(User user, User userToCheck) {
        if (userToCheck.getUserId() != user.getUserId()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_ERROR);
        }
    }
    public void checkAuthor(User user, Post post){
        if (user.getUserId() != post.getCreator().getUserId()){
            throw new UnauthorizedOperationException(AUTHORIZATION_ERROR);
        }
    }
    public void checkAuthor(User user, Comment comment){
        if (user.getUserId() != comment.getCreator().getUserId()){
            throw new UnauthorizedOperationException(AUTHORIZATION_ERROR);
        }
    }

    public void checkAdmin(User user) {
        if (!user.getAdminStatus()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_ERROR);
        }
    }
}
