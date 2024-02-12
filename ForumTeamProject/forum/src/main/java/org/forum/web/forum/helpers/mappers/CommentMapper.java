package org.forum.web.forum.helpers.mappers;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.service.contracts.CommentService;
import org.forum.web.forum.service.contracts.PostService;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final PostService postService;
    private final CommentService commentService;
    public CommentMapper(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    public Comment fromDto(CommentDTO commentDTO, int id) {
        Comment comment = commentService.getById(id);
        comment.setContent(commentDTO.getContent());
        return comment;
    }

    public Comment fromDto(int postID, CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreationDate(commentDTO.getCreationDate());
        comment.setPost(postService.getById(postID));
        return comment;
    }

    public Comment fromDto(CommentDTO commentDto){
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        return comment;
    }


}

