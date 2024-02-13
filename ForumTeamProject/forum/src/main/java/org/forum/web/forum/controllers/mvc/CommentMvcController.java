package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.export.UnableToRegisterMBeanException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentMvcController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final PostService postService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public CommentMvcController(CommentService commentService,
                                CommentMapper commentMapper,
                                PostService postService,
                                UserService userService,
                                AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/{id}")
    public String showComments(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            List<Comment> comments = commentService.getPostComments(post.getId());
            model.addAttribute("post", postService.getById(id));
            model.addAttribute("comments", comments);
            return "CommentView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/create/{postId}")
    public String handleCreation(Model model, HttpSession session){
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("create", new CommentDTO());
        return "CreateCommentView";
    }
    @PostMapping("/create/{postId}")
    public String handleCreation(@ModelAttribute("create") CommentDTO commentDTO,
                                 Model model,
                                 HttpSession session,
                                 @PathVariable int postId){
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Comment comment = commentMapper.fromDto(postId, commentDTO);
            commentService.create(user, comment);
            model.addAttribute("comment", comment);
            //TODO Should not return HomePageView, use another instead
            return "redirect:/posts/" + postId;
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }


    }
}