package org.forum.web.forum.service;

import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PoseService {

    private PostRepository postRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public Post getById(int id) {
        return null;
    }

    @Override
    public void create(Post post, User user) {

    }

    @Override
    public void update(Post post, User user) {

    }

    @Override
    public void delete(Post post, User user) {

    }
}
