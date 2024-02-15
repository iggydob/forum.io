package org.forum.web.forum.helpers.mappers;

import org.forum.web.forum.models.Dtos.RegisterDto;
import org.forum.web.forum.models.Dtos.UserDto;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.contracts.UserService;
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

    public User dtoUserCreateMvc(RegisterDto registerDto) {
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        return user;
    }

    public User dtoUserUpdate(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
//        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
//        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
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

    public User dtoUserPassword(UserDto userDto) {
        User user = new User();
        user.setPassword(userDto.getPassword());
        return user;
    }
}
