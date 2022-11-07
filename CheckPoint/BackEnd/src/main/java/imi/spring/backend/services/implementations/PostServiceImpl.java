package imi.spring.backend.services.implementations;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Location;
import imi.spring.backend.models.Post;
import imi.spring.backend.repositories.PostRepository;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.JWTService;
import imi.spring.backend.services.LocationService;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final JWTService jwtService;

    private final LocationService locationService;

    private final AppUserService appUserService;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public String savePost(Post post, Long userId, Long locationId) {
        Location location = locationService.getLocationById(locationId);
        if (location != null) {
            post.setTime(LocalDateTime.now());
            post.setUser(appUserService.getUserById(userId));
            post.setLocation(location);
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

    @Override
    public Long getNumberOfPostsInTotal() {
        return postRepository.count();
    }

    @Override
    public Long getNumberOfPostsPerUser(HttpServletRequest request) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        return postRepository.countAllByUser(user);
    }

    @Override
    public List<Post> getPostsByLocationId(Long locationId) {
        Location location = locationService.getLocationById(locationId);
        if (location != null)
            return postRepository.findAllByLocation(location);
        return Collections.emptyList();
    }
}
