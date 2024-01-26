package org.forum.web.forum.models.Dtos;

import java.sql.Timestamp;

public class CommentDTO {

    private String content;
    private Timestamp creationDate;

    public CommentDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreationDate() {
        return this.creationDate = Timestamp.valueOf(creationDate.toLocalDateTime());
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
