package org.forum.web.forum.models;

import java.util.Optional;

public class UserFilterOptions {
    private final Optional<String> name;
    private final Optional<String> username;
    private final Optional<String> email;

    public UserFilterOptions(String name,
                             String username,
                             String email) {
        this.name = Optional.ofNullable(name);
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

}
