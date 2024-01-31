package org.forum.web.forum.helpers.mappers;

import org.forum.web.forum.models.Dtos.PostDto;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.service.contracts.PostService;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final PostService service;

    public PostMapper(PostService service) {
        this.service = service;
    }

    public Post fromDto(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;
    }

    public Post fromDto(int id, PostDto postDto) {
        Post post = service.getById(id);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;
    }
}
