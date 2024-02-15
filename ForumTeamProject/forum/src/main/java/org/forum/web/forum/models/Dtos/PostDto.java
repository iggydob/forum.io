package org.forum.web.forum.models.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

public class PostDto {
    @NotNull(message = "Title can't be empty!")
    @Size(min = 16, max = 64, message = "Title should be between 16 and 64 characters long!")
    private String title;
    @NotNull(message = "Content can't be empty!")
    @Size(min = 32, max = 8192, message = "Content should be between 32 and 8192 characters long!")
    private String content;
    private Timestamp creationDate;

    public PostDto() {
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = Timestamp.valueOf(creationDate.toLocalDateTime());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
