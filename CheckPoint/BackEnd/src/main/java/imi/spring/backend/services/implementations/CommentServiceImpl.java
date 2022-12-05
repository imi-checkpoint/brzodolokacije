package imi.spring.backend.services.implementations;

import imi.spring.backend.models.*;
import imi.spring.backend.repositories.CommentRepository;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.CommentService;
import imi.spring.backend.services.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public String addComment(String commentText, Long userId, Long postId, Long parentCommentId) {
        Comment comment = new Comment();
        comment.setText(commentText.trim());
        comment.setTime(LocalDateTime.now());
        comment.setUser(appUserService.getUserById(userId));
        comment.setPost(postService.getPostById(postId));
        if (parentCommentId == 0) // PARENTCOMMENTID = 0 KADA NEMA PARENTCOMMENT !!!
            comment.setParentComment(null);
        else {
            Comment parentComment = getCommentById(parentCommentId);
            if (parentComment == null)
                return "Parent comment with that id does not exist!";
            comment.setParentComment(parentComment);
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


    @Override
    public CommentDTO convertCommentToCommentDTO(AppUser userFromJWT, Comment comment) throws IOException {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        AppUser commentAuthor = comment.getUser();
        commentDTO.setAuthorId(commentAuthor.getId());
        commentDTO.setAuthorUsername(commentAuthor.getUsername());
        commentDTO.setPostId(comment.getPost().getId());
        List<Comment> sortedChildren = comment.getSubCommentList().stream()
                .sorted(Comparator.comparing(Comment::getTime))
                .collect(Collectors.toList());
        commentDTO.setSubCommentList(convertListOfCommentsToCommentDTOs(userFromJWT, sortedChildren));

        /*CommentLike like = comment.getCommentLikeList()
                .stream()
                .filter(commentLike -> commentLike.getUser().equals(userFromJWT))
                .collect(Collectors.toList()).stream().findFirst().orElse(null);
        commentDTO.setIsLiked(like != null);*/

        return  commentDTO;
    }

    @Override
    public List<CommentDTO> convertListOfCommentsToCommentDTOs(AppUser userFromJWT, List<Comment> comments) throws IOException {
        List<CommentDTO> commentDTOs = new ArrayList<>();

        for(Comment comment : comments){
            commentDTOs.add(convertCommentToCommentDTO(userFromJWT, comment));
        }

        return  commentDTOs;
    }

    @Override
    public List<Comment> getFirstCommentsByPostId(Long postId) {
        if (postService.getPostById(postId) == null)
            return Collections.emptyList();
        return commentRepository.findAllByPostIdOrderByTimeAsc(postId).stream()
                .filter(comment -> comment.getParentComment() == null)
                .collect(Collectors.toList());
    }
}
