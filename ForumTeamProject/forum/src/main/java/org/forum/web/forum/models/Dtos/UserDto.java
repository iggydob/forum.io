package org.forum.web.forum.models.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.forum.web.forum.models.PhoneNumber;

public class UserDto {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private PhoneNumber phoneNumber;
    private boolean banStatus;
    private boolean adminStatus;

    public UserDto() {
    }

    //    @NotNull(message = "First name can't be an empty field.")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols.")
    public String getFirstName() {
        return firstName;
    }

    //    @NotNull(message = "Last name can't be an empty field.")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols.")
    public String getLastName() {
        return lastName;
    }

    //    @NotNull(message = "Username can't be an empty field.")
    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols.")
    public String getUsername() {
        return username;
    }

    //    @NotNull(message = "Password can't be an empty field.")
    @Size(min = 8, max = 32, message = "Password should be at least 8 symbols.")
    public String getPassword() {
        return password;
    }

    @Size(max = 10, message = "Phone number should not exceed 10 symbols.")
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    //    @NotNull(message = "E-mail can't be an empty field.")
    @Email(message = "E-mail should be valid.")
    public String getEmail() {
        return email;
    }

    //    @NotNull(message = "Ban status field cannot be an empty field.")
    public boolean getBanStatus() {
        return banStatus;
    }

    public boolean getAdminStatus() {
        return adminStatus;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBanStatus(boolean banStatus) {
        this.banStatus = banStatus;
    }

    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
