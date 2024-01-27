package org.forum.web.forum.service;

import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.LikePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikePostServiceImpl implements LikePostService {
    private final LikePostRepository likePostRepository;

    @Autowired
    public LikePostServiceImpl(LikePostRepository likePostRepository) {
        this.likePostRepository = likePostRepository;
    }


    @Override
    public LikePost get(Post post, User user) {
        return likePostRepository.get(post,user);
    }

    @Override
    public List<LikePost> getByUserId(int id) {
        return likePostRepository.getByUserId(id);
    }

    @Override
    public List<LikePost> getByPostId(int id) {
        return likePostRepository.getByPostId(id);
    }

    @Override
    public LikePost getById(int id) {
        return likePostRepository.getById(id);
    }

    @Override
    public void create(Post post,User user) {
        LikePost likePost = new LikePost();
        likePost.setPost(post);
        likePost.setUser(user);
        likePostRepository.create(likePost);
    }

    @Override
    public void update(LikePost likePost, User user) {
        likePostRepository.update(likePost);
    }

    @Override
    public void delete(LikePost likePost) {
        likePostRepository.delete(likePost);
    }
}
