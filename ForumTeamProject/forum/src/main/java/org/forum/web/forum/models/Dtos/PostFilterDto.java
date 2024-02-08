package org.forum.web.forum.models.Dtos;

public class PostFilterDto {
    private String  title;
    private String postAuthor;
    private String sortPostBy;
    private String sortOrder;

    public PostFilterDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getSortPostBy() {
        return sortPostBy;
    }

    public void setSortPostBy(String sortPostBy) {
        this.sortPostBy = sortPostBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
