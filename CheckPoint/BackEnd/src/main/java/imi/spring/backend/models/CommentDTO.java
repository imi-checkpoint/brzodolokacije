package imi.spring.backend.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private Long authorId;
    private String authorUsername;
    private Long postId;
    private List<CommentDTO> subCommentList;
    private String text;
    //private LocalDateTime time;
    //private Boolean isLiked; //da li je logovani user lajkovao ovaj komentar
}
