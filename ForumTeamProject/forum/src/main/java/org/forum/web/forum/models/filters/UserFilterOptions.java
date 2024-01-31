package org.forum.web.forum.models.filters;

import java.util.Optional;

public class UserFilterOptions {
    private final Optional<String> firstName;
    private final Optional<String> lastName;
    private final Optional<String> username;
    private final Optional<String> email;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;

    public UserFilterOptions(String firstName,
                             String lastName,
                             String username,
                             String email,
                             String sortBy,
                             String sortOrder) {
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
