package imi.spring.backend.repositories;

import imi.spring.backend.models.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    List<PostLike> findAllByPostId(Long postId);

    Integer countByPostId(Long postId);

    PostLike findByPostIdAndUserId(Long postId, Long userId);
}
