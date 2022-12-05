package imi.spring.backend.controllers;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Post;
import imi.spring.backend.models.PostDTO;
import imi.spring.backend.services.JWTService;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;
    private final JWTService jwtService;

    @GetMapping("/all")
    @ResponseBody
    public List<PostDTO> getAllPosts() throws IOException {
        log.info("Getting all posts.");
        return postService.convertListOfPostsToPostDTOs(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public PostDTO getPostById(@PathVariable Long id) throws IOException {
        return postService.convertPostToPostDTO(postService.getPostById(id));
    }

    @GetMapping("/{id}/comments/count")
    @ResponseBody
    public Integer countCommentsByPostId(@PathVariable Long id) { return postService.countCommentsByPostId(id); }

    /*
    @PostMapping("/save/location/{locationId}")
    @ResponseBody
    public String savePost(HttpServletRequest request, @RequestBody Post post, @PathVariable Long locationId) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.savePost(post, user.getId(), locationId);
        return "Invalid user!";
    }*/

    @PostMapping("/save/location/{locationId}")
    @ResponseBody
    public String savePost(HttpServletRequest request, @RequestBody String description, @PathVariable Long locationId) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.savePost(description, user.getId(), locationId);
        return "Invalid user!";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deletePostById(HttpServletRequest request, @PathVariable Long id) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.deletePost(user.getId(), id);
        return "Invalid user!";
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<PostDTO> getPostsByUserId(@PathVariable Long userId) throws IOException {
        return postService.convertListOfPostsToPostDTOs(postService.getPostsByUserId(userId));
    }

    @GetMapping("/location/{locationId}")
    @ResponseBody
    public List<PostDTO> getPostsByLocationId(@PathVariable Long locationId) throws IOException {
        log.info("Calling this one.");
        return postService.convertListOfPostsToPostDTOs(postService.getPostsByLocationId(locationId));
    }

    @GetMapping("/count/all")
    @ResponseBody
    public Long getNumberOfPostsInTotal() { return postService.getNumberOfPostsInTotal(); }

    @GetMapping("/my/count")
    @ResponseBody
    public Long getNumberOfMyPosts(HttpServletRequest request) throws ServletException { return postService.getNumberOfMyPosts(request); }

    @GetMapping("/user/{userId}/count")
    @ResponseBody
    public Long getNumberOfPostsByUserId(@PathVariable Long userId) { return postService.getNumberOfPostsByUserId(userId); }
}
