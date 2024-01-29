package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;
    private final LikePostService likePostService;
    private final TagService tagService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, LikePostService likePostService, TagService tagService, AuthenticationHelper authenticationHelper) {
        this.postRepository = postRepository;
        this.likePostService = likePostService;
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
    }

    @Override
    public List<Post> getFiltered(PostFilterOptions postFilterOptions) {
        return postRepository.getFiltered(postFilterOptions);
    }

    @Override
    public List<Post> getByUserId(PostFilterOptions postFilterOptions, int id) {
        return postRepository.getByUserId(postFilterOptions, id);
    }

    @Override
    public List<Post> getMostRecent() {
        return postRepository.getRecent();
    }

    @Override
    public List<Post> getMostCommented() {
        return postRepository.getMostCommented();
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void addTagToPost(User userWhoAdds, Post post, Tag tag) {
        authenticationHelper.checkIfBanned(userWhoAdds);
        authenticationHelper.checkAuthor(userWhoAdds, post.getCreator());

        Tag newTag = tagService.create(tag, userWhoAdds);
        post.getTags().add(newTag);
        postRepository.update(post);
    }

    @Override
    public void deleteTagFromPost(User userWhoDeletes, Post postFromWhichToDelete, Tag tag) {
        authenticationHelper.checkIfBanned(userWhoDeletes);
        authenticationHelper.checkAuthor(userWhoDeletes, postFromWhichToDelete.getCreator());

        postFromWhichToDelete.getTags().remove(tag);
        postRepository.update(postFromWhichToDelete);
    }

    @Override
    public void likePost(int id, User user) {
        Post post = postRepository.getById(id);
        try {
            authenticationHelper.checkIfBanned(user);
            LikePost likePost = likePostService.get(post, user);
            post.setNewLike(user);
            likePostService.delete(likePost);
        } catch (EntityNotFoundException e) {
            likePostService.create(post, user);
        }
    }


    @Override
    public void create(Post post, User user) {
        authenticationHelper.checkIfBanned(user);
        post.setCreator(user);
        post.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        postRepository.create(post);

    }

    @Override
    public void update(Post post, User user) {
        authenticationHelper.checkIfBanned(user);
        authenticationHelper.checkAuthor(user, post);
        postRepository.update(post);

    }

    @Override
    public void delete(Post post, User user) {
        try {
            authenticationHelper.checkAdmin(user);
            postRepository.delete(post);
        } catch (AuthorizationException e) {
            authenticationHelper.checkAuthor(user, post);
            postRepository.delete(post);
        }
    }
}
