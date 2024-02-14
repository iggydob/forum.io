package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.web.forum.exceptions.AuthorizationException;
import org.forum.web.forum.exceptions.EntityDuplicateException;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.exceptions.UnauthorizedOperationException;
import org.forum.web.forum.helpers.AuthenticationHelper;
import org.forum.web.forum.helpers.mappers.CommentMapper;
import org.forum.web.forum.helpers.mappers.PostMapper;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Dtos.CommentDTO;
import org.forum.web.forum.models.Dtos.PostDto;
import org.forum.web.forum.models.Dtos.PostFilterDto;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.models.User;
import org.forum.web.forum.models.filters.PostFilterOptions;
import org.forum.web.forum.service.contracts.CommentService;
import org.forum.web.forum.service.contracts.PostService;
import org.forum.web.forum.service.contracts.TagService;
import org.forum.web.forum.service.contracts.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final TagService tagService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CommentMapper commentMapper;
    private final PostMapper postMapper;

    public PostMvcController(PostService postService,
                             CommentService commentService,
                             TagService tagService,
                             UserService userService,
                             AuthenticationHelper authenticationHelper,
                             CommentMapper commentMapper,
                             PostMapper postMapper) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterOptions") PostFilterDto filterDto, Model model, HttpSession session) {
        PostFilterOptions filterOptions = new PostFilterOptions(
                filterDto.getTitle(),
                filterDto.getPostAuthor(),
                filterDto.getSortPostBy(),
                filterDto.getSortOrder());
        List<Post> posts = postService.getFiltered(filterOptions);
        if (populateIsAuthenticated(session)) {
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByUsername(currentUsername));
        } else {
            model.addAttribute("mostCommentedPosts", postService.getMostCommented());
            model.addAttribute("mostRecentPosts", postService.getMostRecent());
            model.addAttribute("postCount", postService.getPostCount());
            return "HomePageNotLogged";
        }
        model.addAttribute("filterOptions", filterDto);
        model.addAttribute("posts", posts);
        model.addAttribute("postCount", postService.getPostCount());
        return "HomePageView";
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
            Post post = postService.getById(id);
            User user = post.getCreator();
            //todo think about comments and tags
            List<Comment> comment = commentService.getPostComments(post.getId());
            Set<Tag> tags = post.getTags();
            model.addAttribute("post", post);
            model.addAttribute("comments", comment);
            model.addAttribute("tags", tags);
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
    @GetMapping("/new")
    public String showNewPostPage(@Valid @ModelAttribute("newPost") PostDto postDto,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpSession session){
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("post", new PostDto());
        return "PostCreateView";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("newPost") PostDto postDto,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {
      User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }



        if (populateIsAuthenticated(session)) {
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.getByUsername(currentUsername));
        }

        if (bindingResult.hasErrors()) {
            return "PostCreateView";
        }

        try {
            Post post = postMapper.fromDto(postDto);
            postService.create(post, user);
            model.addAttribute("post", post);
            return "HomePageView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_post", e.getMessage());
            return "PostCreateView";
        }
    }
}
