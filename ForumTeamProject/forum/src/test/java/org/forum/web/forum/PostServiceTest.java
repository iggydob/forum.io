package org.forum.web.forum;

import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.exceptions.UnauthorizedOperationException;
import org.forum.web.forum.helpers.AuthorizationHelper;
import org.forum.web.forum.models.LikePost;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.repository.contracts.PostRepository;
import org.forum.web.forum.service.PostServiceImpl;
import org.forum.web.forum.service.contracts.LikePostService;
import org.forum.web.forum.service.contracts.TagService;
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
public class PostServiceTest {
    @Mock
    PostRepository mockRepository;

    @Mock
    AuthorizationHelper authorizationHelper;

    @Mock
    LikePostService mockPostLikeService;

    @Mock
    TagService tagService;

    @InjectMocks
    PostServiceImpl service;

    @Test
    public void get_Should_CallRepository() {
        //Arrange
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        when(mockRepository.getFiltered(filterOptions))
                .thenReturn(null);

        //Act
        service.getFiltered(filterOptions);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getFiltered(filterOptions);
    }

    @Test
    public void getById_Should_CallRepository() {
        //Arrange
        when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(null);

        //Act
        service.getById(Mockito.anyInt());

        Mockito.verify(mockRepository, Mockito.times(1)).getById(Mockito.anyInt());
    }

    @Test
    public void getById_Should_Throw_WhenNoMatchFound() {
        //Arrange
        when(mockRepository.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.getById(2));
    }

    @Test
    public void getById_Should_ReturnPost_When_MatchByIdExists() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockPost);

        //Act
        Post result = service.getById(Mockito.anyInt());

        //Assert
        Assertions.assertEquals(mockPost, result);
    }

    @Test
    public void getByUserId_Should_CallRepository() {
        //Arrange
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenReturn(null);

        //Act
        service.getByUserId(filterOptions, 2);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getByUserId(Mockito.eq(filterOptions), Mockito.anyInt());
    }

    @Test
    public void getByUserId_Should_ReturnPostList_WhenUserExists() {
        //Arrange
        List<Post> mockPostList = Helpers.createMockPostList();
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        when(mockRepository.getByUserId(Mockito.eq(filterOptions), Mockito.anyInt()))
                .thenReturn(mockPostList);

        //Act
        List<Post> result = service.getByUserId(filterOptions, 2);


        //Assert
        Assertions.assertEquals(mockPostList, result);
    }

    @Test
    public void getByUserId_Should_Throw_WhenUserDoesNotExist() {
        //Arrange
        PostFilterOptions filterOptions = Helpers.createMockPostFilterOptions();
        when(mockRepository.getByUserId(Mockito.any(PostFilterOptions.class), Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.getByUserId(filterOptions, 2));
    }

    @Test
    public void create_Should_CallRepository() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Mockito.doNothing().when(mockRepository).create(Mockito.any(Post.class));

        //Act
        service.create(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).create(mockPost);
    }

    @Test
    public void create_Should_SetUserOfPost_WhenInvoked() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        Mockito.doNothing().when(mockRepository).create(Mockito.any(Post.class));

        //Act
        service.create(mockPost, mockUser);

        //Assert
        Assertions.assertEquals(mockPost.getCreator(), mockUser);
    }

    @Test
    public void create_Should_Throw_WhenUserBlocked() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockBanUser();


        doThrow(UnauthorizedOperationException.class).when(authorizationHelper).checkIfBanned(Mockito.any(User.class));

        //Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> service.create(mockPost, mockUser));
    }

    @Test
    public void update_Should_CallRepository_WhenUserIsCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

        Mockito.doNothing().when(authorizationHelper).checkAuthor(Mockito.any(User.class), Mockito.any(Post.class));

        Mockito.doNothing().when(mockRepository).update(mockPost);

        mockPost.setCreator(mockUser);

        //Act
        service.update(mockPost, mockUser);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }

    @Test
    public void update_Should_Throw_WhenUserIsNotAdminOrCreator() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();

        doThrow(UnauthorizedOperationException.class).when(authorizationHelper).checkAuthor(Mockito.any(User.class), Mockito.any(Post.class));

        //Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> service.update(mockPost, mockUser));
    }
    @Test
    public void likePost_Should_Throw_WhenPostDoesNotExist() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        when(mockRepository.getById(Mockito.anyInt())).thenThrow(EntityNotFoundException.class);

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.likePost(2, mockUser));
    }
    @Test
    public void likePost_Should_CallLikeServiceToDelete_WhenLikeExist() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        LikePost mockLike = Helpers.createMockLike();
        when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        when(mockPostLikeService.get(mockPost, mockUser)).thenReturn(mockLike);

        //Act
        service.likePost(2, mockUser);

        //Assert
        Mockito.verify(mockPostLikeService, Mockito.times(1)).delete(mockLike);

    }
    @Test
    public void likePost_Should_CallLikeServiceToCreate_WhenLikeDoesNotExist() {
        //Arrange
        Post mockPost = Helpers.createMockPost();
        User mockUser = Helpers.createMockUser();
        when(mockRepository.getById(Mockito.anyInt())).thenReturn(mockPost);

        when(mockPostLikeService.get(mockPost, mockUser)).thenThrow(EntityNotFoundException.class);

        //Act
        service.likePost(2,mockUser);

        //Assert
        Mockito.verify(mockPostLikeService, Mockito.times(1)).create(mockPost, mockUser);
    }
    @Test
    public void getMostCommented_Should_CallRepository() {
        //Arrange
        when(mockRepository.getMostCommented()).thenReturn(null);

        //Act
        service.getMostCommented();

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).getMostCommented();
    }
    @Test
    public void getMostRecentlyCreatedPosts_Should_CallRepository() {
        //Arrange
        when(mockRepository.getRecent()).thenReturn(null);

        //Act
        service.getMostRecent();

        //Assert
        Mockito.verify(mockRepository,Mockito.times(1)).getRecent();
    }
    @Test
    public void addTagToPost_Should_Throw_WhenUserIsBanned() {
        //Arrange
        User mockAuthenticatedUser = Helpers.createMockBanUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();

        doThrow(UnauthorizedOperationException.class).when(authorizationHelper).checkIfBanned(Mockito.any(User.class));

        //Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> service.addTagToPost(mockAuthenticatedUser, mockPost, mockTag));
    }
    @Test
    void addTagToPost_Should_Throw_WhenUserIsNotCreator() {
        // Arrange
        User userWhoAdds = mock(User.class);
        Post post = mock(Post.class);
        Tag tag = mock(Tag.class);

        User creator = mock(User.class);
        when(post.getCreator()).thenReturn(creator);

        doThrow(new UnauthorizedOperationException("User is not the creator"))
                .when(authorizationHelper)
                .checkAuthor(eq(userWhoAdds), eq(creator));

        // Act and Assert
        assertThrows(UnauthorizedOperationException.class, () -> {
            service.addTagToPost(userWhoAdds, post, tag);
        });

        // Verify that the authorizationHelper methods were called
        verify(authorizationHelper).checkIfBanned(userWhoAdds);
        verify(authorizationHelper).checkAuthor(userWhoAdds, creator);

        // Verify that other methods were not called
        verifyNoMoreInteractions(authorizationHelper, tagService, mockRepository);
    }

    @Test
    public void addTagToPost_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        when(tagService.create(Mockito.any(Tag.class), Mockito.any(User.class))).thenReturn(mockTag);

        //Act
        service.addTagToPost(mockUser, mockPost, mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }
    @Test
    public void deleteTagFromPost_Should_Throw_WhenUserIsBanned() {
        //Arrange
        User mockUser = Helpers.createMockBanUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        doThrow(UnauthorizedOperationException.class).when(authorizationHelper).checkIfBanned(Mockito.any(User.class));


        //Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> service.deleteTagFromPost(mockUser, mockPost, mockTag));
    }
    @Test
    public void deleteTagFromPost_Should_CallRepository() {
        //Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        //Act
        service.deleteTagFromPost(mockUser, mockPost, mockTag);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).update(mockPost);
    }
}
