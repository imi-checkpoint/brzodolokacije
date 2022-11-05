package imi.spring.backend.controllers;

import imi.spring.backend.models.Post;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/all")
    @ResponseBody
    public List<Post> getAllPosts() { return postService.getAllPosts(); }

    @GetMapping("/{id}")
    @ResponseBody
    public Post getPostById(@PathVariable Long id) { return postService.getPostById(id); }

    @PostMapping("/save")
    @ResponseBody
    public String savePost(HttpServletRequest request, @RequestBody Post post) throws ServletException { return postService.savePost(request, post); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deletePostById(@PathVariable Long id) { return postService.deletePost(id); }

    @GetMapping("/count/all")
    @ResponseBody
    public Long getNumberOfPostsInTotal() { return postService.getNumberOfPostsInTotal(); }

    @GetMapping("/count")
    @ResponseBody
    public Long getNumberOfPostsPerUser(HttpServletRequest request) throws ServletException { return postService.getNumberOfPostsPerUser(request); }
}
