package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.exceptions.UnauthorizedOperationException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.CommentMapper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.User;
import org.forum.web.forum.service.contracts.CommentService;
import org.forum.web.forum.service.contracts.PostService;
import org.forum.web.forum.service.contracts.TagService;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final TagService tagService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CommentMapper commentMapper;

    public PostMvcController(PostService postService,
                             CommentService commentService,
                             TagService tagService,
                             UserService userService,
                             AuthenticationHelper authenticationHelper,
                             CommentMapper commentMapper) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @PostMapping("/submitComment")
    public String createComment(@ModelAttribute("comment") CommentDTO commentDTO,
                                Model model,
                                HttpServletRequest request,
                                HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            int postId = (int) session.getAttribute("sessionPostId");
            Comment comment = commentMapper.fromDto(postId, commentDTO);
            commentService.create(user, comment);
            model.addAttribute("comment", comment);

            return "redirect:/posts/" + postId;
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user;
            Post post = postService.getById(id);
            //todo think about comments and tags
            List<Comment> comment = commentService.getPostComments(post.getId());
            model.addAttribute("post", post);
            model.addAttribute("comments", comment);
            model.addAttribute("commentDto", new CommentDTO());
            model.addAttribute("user", user);
            session.setAttribute("sessionPostId", id);
            return "PostViewTheme";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
