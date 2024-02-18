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
}


