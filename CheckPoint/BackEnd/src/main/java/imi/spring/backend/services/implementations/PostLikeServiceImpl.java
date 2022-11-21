package imi.spring.backend.services.implementations;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Post;
import imi.spring.backend.models.PostLike;
import imi.spring.backend.repositories.PostLikeRepository;
import imi.spring.backend.services.JWTService;
import imi.spring.backend.services.PostLikeService;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final JWTService jwtService;

    private final PostService postService;

    @Override
    public List<PostLike> getAllPostLikes() { return postLikeRepository.findAll(); }

    @Override
    public List<PostLike> getAllLikesByPostId(Long postId) {
        return postLikeRepository.findAllByPostId(postId);
    }

    @Override
    public Integer getNumberOfLikesByPostId(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    @Override
    public String likeOrUnlikePostById(HttpServletRequest request, Long postId) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        Post post = postService.getPostById(postId);
        if (post == null)
            return "Post with that id does not exist!";
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, user.getId());
        if (postLike == null){ //like
            postLikeRepository.save(new PostLike(post, user, LocalDateTime.now()));
            return "Liked";
        }
        postLikeRepository.delete(postLike); //unlike
        return "Unliked";
    }

    @Override
    public List<Long> getAllLikedPostsIdsByUser(AppUser user, Long postId) {
        List<PostLike> postLikeList = user.getPostLikeList();
        List<Long> postIds = new ArrayList<>();
        for (PostLike pl : postLikeList)
        {
            postIds.add(pl.getPost().getId());
        }
        return postIds;
    }
}
