package imi.spring.backend.services.implementations;

import imi.spring.backend.models.Comment;
import imi.spring.backend.models.Post;
import imi.spring.backend.repositories.CommentRepository;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.CommentService;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AppUserService appUserService;
    private final PostService postService;

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAllCommentsByPostId(Long postId) {
        if (postService.getPostById(postId) == null)
            return Collections.emptyList();
        return commentRepository.findAllByPostIdOrderByTimeAsc(postId);
    }

    @Override
    public List<Comment> getAllSubcommentsByCommentId(Long id) {
        Comment comment = getCommentById(id);
        if (comment == null)
            return Collections.emptyList();
        return comment.getSubCommentList();
    }

    @Override
    public String addComment(Comment comment, Long userId, Long postId, Long parentCommentId) {
        comment.setTime(LocalDateTime.now());
        comment.setUser(appUserService.getUserById(userId));
        comment.setPost(postService.getPostById(postId));
        log.info("Parrent comment id {} u servisu", parentCommentId);
        if (parentCommentId == 0) // PARENTCOMMENTID = 0 KADA NEMA PARENTCOMMENT !!!
            comment.setParentComment(null);
        else {
            log.info("USAO U ELSE, IMA PARRENTCOMMENTID");
            comment.setParentComment(getCommentById(parentCommentId));
        }
        commentRepository.save(comment);
        return "Saved";
    }

    @Override
    public String deleteCommentById(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return "Deleted.";
        }
        return "Comment with that id does not exist!";
    }
}
