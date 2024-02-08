package org.forum.web.forum.models.filters;

import java.util.Optional;

public class PostFilterOptions {
        private Optional<String>title;
        private Optional<String>postAuthor;
        private Optional<String>sortPostBy;
        private Optional<String>sortOrder;

    public PostFilterOptions(String title, String postAuthor,String sortPostBy, String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.postAuthor = Optional.ofNullable(postAuthor);
        this.sortPostBy = Optional.ofNullable(sortPostBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public PostFilterOptions() {
        this(null, null, null, null);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getPostAuthor() {
        return postAuthor;
    }

    public Optional<String> getSortPostBy() {
        return sortPostBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
