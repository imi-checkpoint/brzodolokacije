package imi.spring.backend.controllers;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Comment;
import imi.spring.backend.services.CommentService;
import imi.spring.backend.services.JWTService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(path = "/comments")
public class CommentController {

    private final CommentService commentService;
    private final JWTService jwtService;

    @GetMapping("/all")
    @ResponseBody
    public List<Comment> getAllComments() { return commentService.getAllComments(); }

    @GetMapping("/{id}")
    @ResponseBody
    public Comment getCommentById(@PathVariable Long id) { return commentService.getCommentById(id); }

    @GetMapping("/all/{postId}")
    @ResponseBody
    public List<Comment> getAllCommentsByPostId(@PathVariable Long postId) { return commentService.getAllCommentsByPostId(postId); }

    @PostMapping("/{postId}/{parentCommentId}/add")
    @ResponseBody
    public String addComment(HttpServletRequest request, @RequestBody String commentText, @PathVariable Long postId, @PathVariable Long parentCommentId) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return commentService.addComment(commentText, user.getId(), postId, parentCommentId);
        return "Invalid user!";
    }

    @GetMapping("/{id}/subcomments")
    @ResponseBody
    public List<Comment> getAllSubcommentsByCommentId(@PathVariable Long id) { return commentService.getAllSubcommentsByCommentId(id); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteCommentById(@PathVariable Long id) { return commentService.deleteCommentById(id); }
}
