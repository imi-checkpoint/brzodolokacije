package imi.spring.backend.services.implementations;

import imi.spring.backend.models.Post;
import imi.spring.backend.repositories.PostRepository;
import imi.spring.backend.services.JWTService;
import imi.spring.backend.services.LocationService;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final JWTService jwtService;

    private final LocationService locationService;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public String savePost(HttpServletRequest request, Post post) throws ServletException {
        if (locationService.getLocationById(post.getLocation().getId()) != null) {
            post.setTime(LocalDateTime.now());
            post.setUser(jwtService.getAppUserFromJWT(request));
            postRepository.save(post);
            return "Saved";
        }
        return "Location with that id does not exist!";
    }

    @Override
    public String deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return "Deleted";
        }
        return "Post with that id does not exist!";
    }

}
