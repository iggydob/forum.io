package org.forum.web.forum.helpers;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.service.CommentService;
import org.forum.web.forum.service.PostService;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final PostService postService;

    public CommentMapper(PostService postService) {
        this.postService = postService;
    }

    public Comment fromDto(int id, int postID, CommentDTO commentDTO) {
        Comment comment = fromDto(postID, commentDTO);
        comment.setId(id);
        return comment;
    }

    public Comment fromDto(int postID, CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreationDate(commentDTO.getCreationDate());
        comment.setPost(postService.getById(postID));
        return comment;
    }


}

