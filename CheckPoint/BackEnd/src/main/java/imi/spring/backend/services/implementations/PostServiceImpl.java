package imi.spring.backend.services.implementations;

import imi.spring.backend.models.*;
import imi.spring.backend.repositories.PostRepository;
import imi.spring.backend.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final JWTService jwtService;
    private final LocationService locationService;
    private final AppUserService appUserService;
    private final PhotoService photoService;
    private final VideoService videoService;
    private final FollowersService followersService;
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

    /*
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
    */
    @Override
    public Long savePost(String description, Long userId, Long locationId) {
        Location location = locationService.getLocationById(locationId);
        if (location != null) {
            Post p = postRepository.save(new Post(description.trim(), LocalDateTime.now(), appUserService.getUserById(userId), location));
            return p.getId();
        }
        return -1l;
    }

    @Override
    public String deletePost(Long userId, Long postId) {
        Post post = getPostById(postId);
        if (post == null)
            return "Post with that id does not exist!";
        if (!(post.getUser().getId().equals(userId)))
            return "This user is not the owner!";
        
        postRepository.deleteById(postId);
        photoService.deletePhotosByPostId(postId);
        return "Deleted";
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
    public PostDTO convertPostToPostDTO(AppUser userFromJWT, Post post) throws IOException {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getId());
        postDTO.setAppUserId(post.getUser().getId());
        postDTO.setAppUserUsername(post.getUser().getUsername());
        postDTO.setLocation(post.getLocation());
        postDTO.setDescription(post.getDescription());
        postDTO.setNumberOfLikes(post.getPostLikeList().size());
        postDTO.setNumberOfComments(post.getCommentList().size());
        PostLike like = post.getPostLikeList()
                .stream()
                .filter(postLike -> postLike.getUser().equals(userFromJWT))
                .collect(Collectors.toList()).stream().findFirst().orElse(null);
        postDTO.setIsLiked(like != null);

        postDTO.setPhotos(photoService.getPhotosByPostId(post.getId()));
        postDTO.setVideos(videoService.getVideosByPostId(post.getId()));

        return  postDTO;
    }

    @Override
    public List<PostDTO> convertListOfPostsToPostDTOs(AppUser userFromJWT, List<Post> posts) throws IOException {
        List<PostDTO> postDTOs = new ArrayList<>();

        for(Post post : posts){
            postDTOs.add(convertPostToPostDTO(userFromJWT, post));
        }

        return  postDTOs;
    }

    @Override
    public List<Post> getPostsOfUsersThatIFollow(AppUser userFromJWT) {
        List<Post> posts = new ArrayList<>();

        List<AppUser> followingUsers = followersService.getMyFollowing(userFromJWT);

        for(AppUser user : followingUsers){
            posts.addAll(this.getPostsByUserId(user.getId()));
        }

        Collections.sort(
                posts,
                ((o1, o2) -> (int) (o2.getId() - o1.getId()))
        );

        return posts;
    }
}