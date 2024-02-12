package org.forum.web.forum.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.forum.web.forum.exceptions.EntityNotFoundException;
import org.forum.web.forum.models.Comment;
import org.forum.web.forum.models.Post;
import org.forum.web.forum.models.Tag;
import org.forum.web.forum.service.contracts.CommentService;
import org.forum.web.forum.service.contracts.PostService;
import org.forum.web.forum.service.contracts.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final TagService tagService;

    public PostMvcController(PostService postService, CommentService commentService, TagService tagService) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
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
    public String showSinglePost(@PathVariable int id, Model model) {
        try {
            Post post = postService.getById(id);
            //todo think about comments and tags
            List<Comment> comment = commentService.getPostComments(post.getId());
            model.addAttribute("post", post);
            model.addAttribute("comments",comment);
            return "PostViewTheme";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
