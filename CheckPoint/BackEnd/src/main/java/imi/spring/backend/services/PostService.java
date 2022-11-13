package imi.spring.backend.services;

import imi.spring.backend.models.Post;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    Integer countCommentsByPostId(Long id);
    String savePost(Post post, Long userId, Long locationId);
    String deletePost(Long id);
    List<Post> getPostsByUserId(Long userId);
    List<Post> getPostsByLocationId(Long locationId);
    Long getNumberOfPostsInTotal();
    Long getNumberOfPostsPerUser(HttpServletRequest request) throws ServletException;
}
