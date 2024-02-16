package org.forum.web.forum.models.Dtos;

public class UserFilterDto {

    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String sortBy;
    private final String sortOrder;

    public UserFilterDto(String firstName,
                         String lastName,
                         String username,
                         String email,
                         String sortBy,
                         String sortOrder) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
