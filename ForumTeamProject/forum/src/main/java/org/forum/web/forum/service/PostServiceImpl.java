package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;

@Service
public class PostServiceImpl implements PoseService {

    private static final String AUTHORIZATION_ERROR = "You are not authorized!";
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<Post> getAll() {
        return postRepository.getAll();
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {
        checkIfBanned(user);
        try {
            if (post.getTitle().isEmpty() || post.getContent().isEmpty()) {
                throw new InputMismatchException("You can't create post without Title and Content");
            }
        } catch (InputMismatchException e) {
            throw new RuntimeException(e.getMessage());
        }
        post.setCreator(user);
        postRepository.create(post);

    }

    @Override
    public void update(Post post, User user) {
        checkIfBanned(user);
        checkAuthor(post.getCreator(), user);
        postRepository.update(post);

    }

    @Override
    public void delete(Post post, User user) {
        checkAdmin(user);
        checkAuthor(post.getCreator(), user);
        checkIfBanned(user);
        postRepository.delete(post);
    }

    private void checkIfBanned(User user) {
        if (user.isBanned()) {
            throw new AuthorizationException("This user is banned!");
        }
    }

    private void checkAuthor(User user, User userToCheck) {
        if (userToCheck.getUserId() != user.getUserId()) {
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
    }

    private void checkAdmin(User user) {
        if (!user.isAdmin()) {
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
    }
}
