package org.forum.web.forum.helpers;

import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User dtoUserCreate(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public User dtoUserUpdate(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public User dtoUserBanStatus(UserDto userDto) {
        User user = new User();
        user.setBanStatus(userDto.getBanStatus());
        return user;
    }

    public User dtoUserAdminStatus(UserDto userDto) {
        User user = new User();
        user.setAdminStatus(userDto.getAdminStatus());
        return user;
    }
}
