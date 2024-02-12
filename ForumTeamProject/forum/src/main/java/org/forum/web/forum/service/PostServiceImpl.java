package org.forum.web.forum.service;

import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.repository.contracts.PostRepository;
import org.forum.web.forum.service.contracts.LikePostService;
import org.forum.web.forum.service.contracts.PostService;
import org.forum.web.forum.service.contracts.TagService;
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
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, LikePostService likePostService, TagService tagService,AuthorizationHelper authenticationHelper) {
        this.postRepository = postRepository;
        this.likePostService = likePostService;
        this.tagService = tagService;
        this.authorizationHelper = authenticationHelper;
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
        authorizationHelper.checkIfBanned(userWhoAdds);
        authorizationHelper.checkAuthor(userWhoAdds, post.getCreator());

        Tag newTag = tagService.create(tag, userWhoAdds);
        post.getTags().add(newTag);
        postRepository.update(post);
    }

    @Override
    public void deleteTagFromPost(User userWhoDeletes, Post postFromWhichToDelete, Tag tag) {
        authorizationHelper.checkIfBanned(userWhoDeletes);
        authorizationHelper.checkAuthor(userWhoDeletes, postFromWhichToDelete.getCreator());

        postFromWhichToDelete.getTags().remove(tag);
        postRepository.update(postFromWhichToDelete);
    }

    @Override
    public void likePost(int id, User user) {
        Post post = postRepository.getById(id);

        try {
            authorizationHelper.checkIfBanned(user);
            LikePost likePost = likePostService.get(post, user);
            post.setNewLike(user);
            likePostService.delete(likePost);
        } catch (EntityNotFoundException e) {
            likePostService.create(post, user);
        }
    }

    @Override
    public long getPostCount() {
        return postRepository.getPostCount();
    }


    @Override
    public void create(Post post, User user) {
        authorizationHelper.checkIfBanned(user);
        post.setCreator(user);
        post.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        postRepository.create(post);

    }

    @Override
    public void update(Post post, User user) {
        authorizationHelper.checkIfBanned(user);
        authorizationHelper.checkAuthor(user, post);
        postRepository.update(post);

    }
    //Hard delete
//    @Override
//    public void delete(Post post, User user) {
//        try {
//            authenticationHelper.checkAdmin(user);
//            postRepository.delete(post);
//        } catch (UnauthorizedOperationException e) {
//            authenticationHelper.checkAuthor(user, post);
//            postRepository.delete(post);
//        }
//    }

    //soft delete
    @Override
    public void delete (User user, int id){
        Post post = postRepository.getById(id);
//        try {
//            authorizationHelper.checkAdmin(user);
//            postRepository.delete(post.getId());
//        } catch (UnauthorizedOperationException e) {
//            authorizationHelper.checkAuthor(user, post);
//            postRepository.delete(post.getId());
//        }

        authorizationHelper.verifyUser(post.getCreator(), user);
        authorizationHelper.checkIfBanned(user);
        post.setDeleted(true);

        postRepository.update(post);
    }
}
