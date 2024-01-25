package org.forum.web.forum.helpers;

import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.service.CommentService;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final CommentService service;

    public CommentMapper(CommentService service) {
        this.service = service;
    }

    public Comment fromDto(int id, CommentDTO commentDTO) {
        Comment comment = fromDto(commentDTO);
        comment.setId(id);
        return comment;
    }

    public Comment fromDto(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setCreationDate(commentDTO.getCreationDate());
        return comment;
    }


}

