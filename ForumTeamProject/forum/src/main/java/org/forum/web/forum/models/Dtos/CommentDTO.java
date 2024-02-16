package org.forum.web.forum.models.Dtos;

import jakarta.validation.constraints.NotEmpty;

import java.sql.Timestamp;

public class CommentDTO {

    @NotEmpty(message = "The content field cannot be empty!")
    private String content;
    private Timestamp creationDate;
//    @Positive(message = "PostId should be positive")
//    private int postID;

    public CommentDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = Timestamp.valueOf(creationDate.toLocalDateTime());
    }
}
