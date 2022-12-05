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
import java.util.Collections;
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
    public List<PostDTO> getAllPosts(HttpServletRequest request) throws IOException, ServletException {
        log.info("Getting all posts.");
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.convertListOfPostsToPostDTOs(user, postService.getAllPosts());
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public PostDTO getPostById(HttpServletRequest request, @PathVariable Long id) throws IOException, ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.convertPostToPostDTO(user, postService.getPostById(id));
        return null;
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
    public Long savePost(HttpServletRequest request, @RequestBody String description, @PathVariable Long locationId) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.savePost(description, user.getId(), locationId);
        return -1l;
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
    public List<PostDTO> getPostsByUserId(HttpServletRequest request, @PathVariable Long userId) throws IOException, ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.convertListOfPostsToPostDTOs(user, postService.getPostsByUserId(userId));
        return Collections.emptyList();
    }

    @GetMapping("/location/{locationId}")
    @ResponseBody
    public List<PostDTO> getPostsByLocationId(HttpServletRequest request, @PathVariable Long locationId) throws IOException, ServletException {
        log.info("Calling this one.");
        AppUser user = jwtService.getAppUserFromJWT(request);
        if (user != null)
            return postService.convertListOfPostsToPostDTOs(user, postService.getPostsByLocationId(locationId));
        return Collections.emptyList();
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
