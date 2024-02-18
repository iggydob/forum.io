package org.forum.web.forum;

import org.forum.web.forum.models.*;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.models.filters.UserFilterOptions;

import java.sql.Timestamp;
import java.util.*;

public class Helpers {
    public static Post createMockPost() {
        var mockPost = new Post();
        Set<LikePost> likes = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        mockPost.setContent("This is a mock content for post creation.");
        mockPost.setTitle("Mock title for mock post.");
        mockPost.setId(1);
        mockPost.setCreator(createMockUser());
        mockPost.setCreationDate(new Timestamp(2024, 2, 17, 13, 13, 13, 13));
        mockPost.setNewLike(createMockUser());
        mockPost.setTags(tags);
        return mockPost;
    }

    public static Comment createMockComment() {
        var mockComment = new Comment();
        Set<Like> likes = new HashSet<>();

        mockComment.setId(1);
        mockComment.setCreator(createMockUser());
        mockComment.setPost(createMockPost());
        mockComment.setContent("This is a mock content for comment creation.");
        mockComment.setCreationDate(new Timestamp(2024, 2, 18, 18, 18, 18, 18));

        return mockComment;
    }

    public static Like createMockLikeComment() {
        var mockLikeComment = new Like();
        mockLikeComment.setId(1);
        mockLikeComment.setUser(createMockUser());
        mockLikeComment.setComment(createMockComment());

        return mockLikeComment;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setAdminStatus(false);
        mockUser.setEmail("mockmail@gmail.com");
        mockUser.setPassword("Aa123456@");
        mockUser.setUsername("mockUsername");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");

        return mockUser;
    }

    public static LikePost createMockLike() {
        var mockLike = new LikePost();
        mockLike.setId(1);
        mockLike.setPost(createMockPost());
        mockLike.setUser(createMockUser());
        return mockLike;
    }

    public static Tag createMockTag() {
        var mockTag = new Tag();
        mockTag.setId(1);
        mockTag.setContent("This is a mock tag.");
        return mockTag;
    }

    public static PostFilterOptions createMockPostFilterOptions() {
        return new PostFilterOptions(
                "user",
                "mockTitle",
                "sort",
                "order"
        );
    }

    public static List<Post> createMockPostList() {
        var mockPostList = new ArrayList<Post>();
        mockPostList.add(createMockPost());
        return mockPostList;
    }

    public static List<Comment> createMockCommentList() {
        var mockCommentList = new ArrayList<Comment>();
        mockCommentList.add(createMockComment());
        return mockCommentList;
    }

    public static User createMockBanUser() {
        var blockUser = new User();
        blockUser.setUserId(3);
        blockUser.setUsername("blockedUser");
        blockUser.setBanStatus(true);
        return blockUser;
    }

    public static User createMockAdminUser() {
        var adminUser = new User();
        adminUser.setUserId(3);
        adminUser.setUsername("adminUser");
        adminUser.setAdminStatus(true);

        return adminUser;
    }

    public static UserFilterOptions createMockUserFilterOptions() {
        return new UserFilterOptions(
                "firstName",
                "lastName",
                "username",
                "email",
                "sortBy",
                "sortOrder"
        );
    }
}
