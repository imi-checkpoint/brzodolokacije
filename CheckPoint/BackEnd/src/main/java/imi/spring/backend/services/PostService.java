package imi.spring.backend.services;

import imi.spring.backend.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post getPostById(Long id);

}
