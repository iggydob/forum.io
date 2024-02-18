package org.forum.web.forum;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.PhoneNumber;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.UserFilterOptions;
import org.forum.web.forum.repository.contracts.UserRepository;
import org.forum.web.forum.service.UserServiceImpl;
import org.forum.web.forum.service.contracts.PhoneNumberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.forum.web.forum.Helpers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository mockRepository;

    @Mock
    PhoneNumberService mockPhoneNumberService;
    @Mock
    AuthorizationHelper mockAuthorizationHelper;
    @InjectMocks
    UserServiceImpl service;

    @Test
    public void create_Should_ThrowEntityDuplicateException_When_UserWithSameUsernameExists() {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockRepository.getByUsername(mockUser.getUsername()))
                .thenReturn(mockUser);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.create(mockUser));
    }

    @Test
    public void create_Should_CallRepository() {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockRepository.getByUsername(mockUser.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", mockUser.getUsername()));

        Mockito.when(mockRepository.getByEmail(mockUser.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "e-mail", mockUser.getEmail()));

        // Act
        service.create(mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockUser);
    }

    @Test
    public void get_Should_CallRepository() {
        // Arrange
        UserFilterOptions mockFilterOptions = createMockUserFilterOptions();
        Mockito.when(mockRepository.getFiltered(mockFilterOptions)).
                thenReturn(null);

        // Act
        service.getFiltered(mockFilterOptions);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .getFiltered(mockFilterOptions);
    }

    @Test
    public void getById_Should_ReturnUser_When_UserIsAdmin() {
        // Arrange
        User adminUser = new User();
        adminUser.setAdminStatus(true);
        User expectedUser = new User();
        when(mockRepository.getById(1)).thenReturn(expectedUser);

        // Act
        User result = service.getById(1, adminUser);

        // Assert
        Assertions.assertEquals(expectedUser, result);
    }

    @Test
    public void updatePhoneNumber_Should_UpdatePhoneNumber_When_NewPhoneNumberIsNotNullAndUserIsAdmin() {
        // Arrange
        User userDetails = new User();
        PhoneNumber newPhoneNumber = new PhoneNumber();
        newPhoneNumber.setPhoneNumber("1234567890");
        userDetails.setPhoneNumber(newPhoneNumber);
        userDetails.setAdminStatus(true);

        User userToUpdate = new User();
        userToUpdate.setAdminStatus(true);

        PhoneNumber oldPhoneNumber = new PhoneNumber();
        oldPhoneNumber.setPhoneNumber("0987654321");
        userToUpdate.setPhoneNumber(oldPhoneNumber);

        // Act
        service.updatePhoneNumber(userDetails, userToUpdate);

        // Assert
        Assertions.assertEquals("1234567890", userToUpdate.getPhoneNumber().getPhoneNumber());
        Mockito.verify(mockPhoneNumberService, Mockito.times(1)).update(Mockito.any(PhoneNumber.class));
    }

    @Test
    public void updateEmail_Should_UpdateEmail_When_NewEmailIsNotNullAndNoDuplicateExists() {
        // Arrange
        User userDetails = new User();
        userDetails.setEmail("newEmail@example.com");

        User userToUpdate = new User();
        userToUpdate.setEmail("oldEmail@example.com");

        Mockito.when(mockRepository.getByEmail(userDetails.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "e-mail", userDetails.getEmail()));

        // Act
        service.updateEmail(userDetails, userToUpdate);

        // Assert
        Assertions.assertEquals("newEmail@example.com", userToUpdate.getEmail());
    }

    @Test
    public void updateEmail_Should_ThrowEntityDuplicateException_When_DuplicateEmailExists() {
        // Arrange
        User userDetails = new User();
        userDetails.setEmail("duplicateEmail@example.com");

        User userToUpdate = new User();
        userToUpdate.setEmail("oldEmail@example.com");

        Mockito.when(mockRepository.getByEmail(userDetails.getEmail()))
                .thenReturn(new User());

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.updateEmail(userDetails, userToUpdate));
    }

    @Test
    public void updatePhoneNumber_Should_NotUpdatePhoneNumber_When_NewPhoneNumberIsNullOrUserIsNotAdmin() {
        // Arrange
        User userDetails = new User();
        userDetails.setPhoneNumber(null);
        userDetails.setAdminStatus(true);

        User userToUpdate = new User();
        userToUpdate.setAdminStatus(false);

        PhoneNumber oldPhoneNumber = new PhoneNumber();
        oldPhoneNumber.setPhoneNumber("0987654321");
        userToUpdate.setPhoneNumber(oldPhoneNumber);

        // Act
        service.updatePhoneNumber(userDetails, userToUpdate);

        // Assert
        Assertions.assertEquals("0987654321", userToUpdate.getPhoneNumber().getPhoneNumber());
        Mockito.verify(mockPhoneNumberService, Mockito.times(0)).update(Mockito.any(PhoneNumber.class));
    }

    @Test
    public void changePassword_Should_UpdatePassword_When_UserDetailsAndPasswordAreNotNull() {
        // Arrange
        int id = 1;
        User userDetails = new User();
        userDetails.setPassword("newPassword");

        User requester = new User();
        requester.setUserId(id);

        User userToUpdate = new User();
        userToUpdate.setPassword("oldPassword");

        Mockito.when(mockRepository.getById(id)).thenReturn(userToUpdate);

        // Act
        service.changePassword(id, userDetails, requester);

        // Assert
        Assertions.assertEquals("newPassword", userToUpdate.getPassword());
        Mockito.verify(mockRepository, Mockito.times(1)).update(userToUpdate);
    }

    @Test
    public void changeAdminStatus_Should_UpdateAdminStatusAndDeletePhoneNumber_When_UserDetailsAndRequesterAreNotNull() {
        // Arrange
        int id = 1;
        User userDetails = new User();
        userDetails.setAdminStatus(true);

        User requester = new User();
        requester.setUserId(id);
        requester.setAdminStatus(true);

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("1234567890");

        User userToUpdate = new User();
        userToUpdate.setAdminStatus(false);
        userToUpdate.setPhoneNumber(phoneNumber);

        Mockito.when(mockRepository.getById(id)).thenReturn(userToUpdate);

        // Act
        service.changeAdminStatus(id, userDetails, requester);

        // Assert
        Assertions.assertTrue(userToUpdate.getAdminStatus());
        Mockito.verify(mockRepository, Mockito.times(1)).update(userToUpdate);
        Mockito.verify(mockPhoneNumberService, Mockito.times(1)).delete(phoneNumber);
    }

    @Test
    public void changeBanStatus_Should_UpdateBanStatus_When_UserDetailsAndRequesterAreNotNull() {
        // Arrange
        int id = 1;
        User userDetails = new User();
        userDetails.setBanStatus(true);

        User requester = new User();
        requester.setUserId(id);
        requester.setAdminStatus(true);

        User userToUpdate = new User();
        userToUpdate.setBanStatus(false);

        Mockito.when(mockRepository.getById(id)).thenReturn(userToUpdate);

        // Act
        service.changeBanStatus(id, userDetails, requester);

        // Assert
        Assertions.assertTrue(userToUpdate.getBanStatus());
        Mockito.verify(mockRepository, Mockito.times(1)).update(userToUpdate);
    }

    @Test
    public void getByUsername_Should_ReturnUser_When_UsernameExists() {
        // Arrange
        String username = "testUser";
        User expectedUser = new User();
        expectedUser.setUsername(username);

        Mockito.when(mockRepository.getByUsername(username)).thenReturn(expectedUser);

        // Act
        User result = service.getByUsername(username);

        // Assert
        Assertions.assertEquals(expectedUser, result);
        Mockito.verify(mockRepository, Mockito.times(1)).getByUsername(username);
    }

    @Test
    public void getAll_Should_ReturnAllUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(new User(), new User(), new User());

        Mockito.when(mockRepository.getAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = service.getAll();

        // Assert
        Assertions.assertEquals(expectedUsers, result);
        Mockito.verify(mockRepository, Mockito.times(1)).getAll();
    }

    @Test
    public void updateFirstName_Should_UpdateFirstName_When_FirstNameIsNotNull() {
        // Arrange
        User userDetails = new User();
        userDetails.setFirstName("NewFirstName");

        User userToUpdate = new User();
        userToUpdate.setFirstName("OldFirstName");

        // Act
        UserServiceImpl.updateFirstName(userDetails, userToUpdate);

        // Assert
        Assertions.assertEquals("NewFirstName", userToUpdate.getFirstName());
    }

    @Test
    public void updateLastName_Should_UpdateLastName_When_LastNameIsNotNull() {
        // Arrange
        User userDetails = new User();
        userDetails.setLastName("NewLastName");

        User userToUpdate = new User();
        userToUpdate.setLastName("OldLastName");

        // Act
        UserServiceImpl.updateLastName(userDetails, userToUpdate);

        // Assert
        Assertions.assertEquals("NewLastName", userToUpdate.getLastName());
    }

    @Test
    public void update_Should_UpdateUserDetails_When_UserDetailsAndRequesterAreNotNull() {
        // Arrange
        int id = 1;
        User userDetails = new User();
        userDetails.setFirstName("NewFirstName");
        userDetails.setLastName("NewLastName");
        userDetails.setEmail("newEmail@example.com");
        userDetails.setPhotoUrl("newPhotoUrl");

        User requester = new User();
        requester.setUserId(id);
        requester.setAdminStatus(true);

        User userToUpdate = new User();
        userToUpdate.setFirstName("OldFirstName");
        userToUpdate.setLastName("OldLastName");
        userToUpdate.setEmail("oldEmail@example.com");
        userToUpdate.setPhotoUrl("oldPhotoUrl");

        Mockito.when(mockRepository.getById(id)).thenReturn(userToUpdate);
        Mockito.when(mockRepository.getByEmail(userDetails.getEmail()))
                .thenThrow(new EntityNotFoundException("User", "e-mail", userDetails.getEmail()));

        // Act
        service.update(id, userDetails, requester);

        // Assert
        Assertions.assertEquals(userDetails.getFirstName(), userToUpdate.getFirstName());
        Assertions.assertEquals(userDetails.getLastName(), userToUpdate.getLastName());
        Assertions.assertEquals(userDetails.getEmail(), userToUpdate.getEmail());
        Assertions.assertEquals(userDetails.getPhotoUrl(), userToUpdate.getPhotoUrl());
        Mockito.verify(mockRepository, Mockito.times(1)).update(userToUpdate);
    }

    @Test
    public void changeAdminStatusMvc_Should_UpdateAdminStatusAndDeletePhoneNumber_When_RequesterIsAdmin() {
        // Arrange
        int id = 1;
        boolean status = true;
        User requester = new User();
        requester.setAdminStatus(true);

        User userToUpdate = new User();
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("1234567890");
        userToUpdate.setPhoneNumber(phoneNumber);

        Mockito.when(mockRepository.getById(id)).thenReturn(userToUpdate);

        // Act
        service.changeAdminStatusMvc(id, status, requester);

        // Assert
        Assertions.assertEquals(status, userToUpdate.getAdminStatus());
        Mockito.verify(mockRepository, Mockito.times(1)).update(userToUpdate);
        Mockito.verify(mockPhoneNumberService, Mockito.times(1)).delete(phoneNumber);
    }

    @Test
    public void changeBanStatusMvc_Should_UpdateBanStatus_When_RequesterIsAdmin() {
        // Arrange
        int id = 1;
        boolean status = true;
        User requester = new User();
        requester.setAdminStatus(true);

        User userToUpdate = new User();
        userToUpdate.setBanStatus(!status);

        Mockito.when(mockRepository.getById(id)).thenReturn(userToUpdate);

        // Act
        service.changeBanStatusMvc(id, status, requester);

        // Assert
        Assertions.assertEquals(status, userToUpdate.getBanStatus());
        Mockito.verify(mockRepository, Mockito.times(1)).update(userToUpdate);
    }
}