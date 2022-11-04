package imi.spring.backend.services;

import imi.spring.backend.models.Post;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post getPostById(Long id);

    String savePost(HttpServletRequest request, Post post) throws ServletException;

    String deletePost(Long id);
}
