package org.forum.web.forum;

import org.forum.web.forum.exceptions.UnauthorizedOperationException;
import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Like;
import org.forum.web.forum.models.User;
import org.forum.web.forum.repository.contracts.CommentRepository;
import org.forum.web.forum.service.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    CommentRepository mockRepository;

    @Mock
    AuthorizationHelper authorizationHelper;

    @InjectMocks
    CommentServiceImpl service;

    @Test
    void get_Should_CallRepository() {

    }

    @Test
    public void get_Should_ReturnComment_When_MatchByIdExist() {
        // Arrange
        Comment mockComment = Helpers.createMockComment();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);

        // Act
        Comment result = service.getById(mockComment.getId());

        // Assert
        Assertions.assertEquals(mockComment, result);
    }
    @Test
    public void create_Should_CallRepository_When_UserIsNotBanned() {
        // Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockUser();

        Mockito.doNothing().when(mockRepository).create(Mockito.any(Comment.class));

        // Act
        service.create(mockUser, mockComment);

        // Assert
        verify(mockRepository, times(1))
                .create(mockComment);
    }

    @Test
    public void create_Should_Throw_When_UserIsBanned() {
        // Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockBanUser();

        // Act
        doThrow(UnauthorizedOperationException.class).when(authorizationHelper).checkIfBanned(Mockito.any(User.class));

        //Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> service.create(mockUser, mockComment));
    }

    @Test
    public void update_Should_CallRepository_WhenUserIsCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();

//        Mockito.doNothing().when(authorizationHelper).checkAuthor(Mockito.any(User.class), Mockito.any(Comment.class));

        Mockito.doNothing().when(mockRepository).update(mockComment);

        mockComment.setCreator(mockUser);

        //Act
        service.update(mockUser, mockComment);

        //Assert
        verify(mockRepository, times(1)).update(mockComment);
    }

    @Test
    public void update_Should_Throw_When_UserIsBanned() {
        // Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockBanUser();

        // Act
        doThrow(UnauthorizedOperationException.class).when(authorizationHelper).checkIfBanned(Mockito.any(User.class));

        //Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> service.update(mockUser, mockComment));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Comment mockComment = Helpers.createMockComment();
        User mockUser = Helpers.createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);

        // Act
        service.delete(mockUser, 1);

        // Assert
        mockComment.setDeleted(true);
        verify(mockRepository, times(1)).update(mockComment);

    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = Helpers.createMockAdminUser();
        Comment mockComment = Helpers.createMockComment();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockComment);

        // Act
        service.delete(mockUserAdmin, 1);

        // Assert
        mockComment.setDeleted(true);
        verify(mockRepository, times(1)).update(mockComment);

    }

    @Test
    public void getPostComments_Should_ReturnCommentList() {
        //Arrange
        List<Comment> mockCommentList = Helpers.createMockCommentList();

        when(mockRepository.getPostComments(1)).thenReturn(mockCommentList);

        //Act
        List<Comment> result = service.getPostComments(1);

        //Assert
        Assertions.assertEquals(mockCommentList, result);
    }

    @Test
    public void commentLikesCount_Should_ReturnLikesCount() {
        //Arrange
        Like mockCommentLike = Helpers.createMockLikeComment();
        Comment mockComment = Helpers.createMockComment();
        mockComment.getLikedList().add(mockCommentLike);
        long count = mockComment.getLikedList().size();

        when(mockRepository.commentLikesCount(Mockito.anyInt())).thenReturn(count);

        //Act
        long result = service.commentLikesCount(1);

        //Assert
        Assertions.assertEquals(count, result);
    }
    @Test
    public void likeComment_Should_AddLike_When_NotLikedBefore() {
        // Arrange
        int commentId = 1;
        User user = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();
        Like mockLike = Helpers.createMockLikeComment();

        when(mockRepository.getById(commentId)).thenReturn(mockComment);

        // Act
        service.likeComment(commentId, user);

        // Assert
        verify(mockRepository, times(1)).getById(commentId);
        verify(mockRepository, times(1)).update(mockComment);
    }
}
