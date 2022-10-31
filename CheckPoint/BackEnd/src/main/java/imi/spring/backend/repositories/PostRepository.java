package imi.spring.backend.repositories;

import imi.spring.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

}
