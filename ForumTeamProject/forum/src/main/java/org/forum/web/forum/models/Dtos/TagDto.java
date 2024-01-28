package org.forum.web.forum.models.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagDto {
    @NotNull(message = "Tag can't be empty! You need to provide content!")
    @Size(max = 20, message = "Tag size can't be more than 20 symbols!")
    private String content;

    public TagDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
