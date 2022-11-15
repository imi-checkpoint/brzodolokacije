package imi.spring.backend.services.implementations;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Location;
import imi.spring.backend.models.Post;
import imi.spring.backend.models.PostDTO;
import imi.spring.backend.repositories.PostRepository;
import imi.spring.backend.services.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JWTService jwtService;
    private final LocationService locationService;
    private final AppUserService appUserService;
    private final PhotoService photoService;
    private final VideoService videoService;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Integer countCommentsByPostId(Long id) {
        Post post = getPostById(id);
        if (post != null)
            return post.getCommentList().size();
        return 0;
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
    public List<Post> getPostsByUserId(Long userId) {
        AppUser user = appUserService.getUserById(userId);
        if (user != null)
            return user.getPostList();
        return Collections.emptyList();
    }

    @Override
    public List<Post> getPostsByLocationId(Long locationId) {
        Location location = locationService.getLocationById(locationId);
        if (location != null)
            return postRepository.findAllByLocation(location);
        return Collections.emptyList();
    }

    @Override
    public Long getNumberOfPostsInTotal() {
        return postRepository.count();
    }

    @Override
    public Long getNumberOfMyPosts(HttpServletRequest request) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        return postRepository.countAllByUser(user);
    }

    @Override
    public Long getNumberOfPostsByUserId(Long userId) {
        AppUser user = appUserService.getUserById(userId);
        if (user != null)
            return postRepository.countAllByUser(user);
        return 0L;
    }

    @Override
    public PostDTO convertPostToPostDTO(Post post) throws IOException {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getId());
        postDTO.setAppUserId(post.getUser().getId());
        postDTO.setAppUserUsername(post.getUser().getUsername());
        postDTO.setLocation(post.getLocation());
        postDTO.setDescription(post.getDescription());
        postDTO.setNumberOfLikes(post.getPostLikeList().size());

        postDTO.setPhotos(photoService.getPhotosByPostId(post.getId()));
        postDTO.setVideos(videoService.getVideosByPostId(post.getId()));

        return  postDTO;
    }

    @Override
    public List<PostDTO> convertListOfPostsToPostDTOs(List<Post> posts) throws IOException {
        List<PostDTO> postDTOs = new ArrayList<>();

        for(Post post : posts){
            postDTOs.add(convertPostToPostDTO(post));
        }

        return  postDTOs;
    }
}
