package org.forum.web.forum.models.Dtos;

import jakarta.validation.constraints.Positive;

import java.sql.Timestamp;

public class CommentDTO {

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
//
//    public int getPostID() {
//        return postID;
//    }
//
//    public void setPostID(int postID) {
//        this.postID = postID;
//    }
}
