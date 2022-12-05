package imi.spring.backend.services;

import imi.spring.backend.models.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments();
    Comment getCommentById(Long id);
    String addComment(Comment comment, Long userId, Long postId, Long parentCommentId);
    String deleteCommentById(Long id);
    List<Comment> getAllCommentsByPostId(Long postId);
    List<Comment> getAllSubcommentsByCommentId(Long id);
}
