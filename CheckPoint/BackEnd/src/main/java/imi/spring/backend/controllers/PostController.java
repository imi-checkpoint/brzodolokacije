package imi.spring.backend.controllers;

import imi.spring.backend.models.Location;
import imi.spring.backend.models.Post;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
