package imi.spring.backend.services.implementations;

import imi.spring.backend.models.Post;
import imi.spring.backend.repositories.PostRepository;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

}
