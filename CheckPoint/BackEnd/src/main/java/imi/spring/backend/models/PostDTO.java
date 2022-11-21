package imi.spring.backend.models;

import imi.spring.backend.models.mongo.Photo;
import imi.spring.backend.models.mongo.Video;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class PostDTO {

    private Long appUserId;
    private String appUserUsername;

    private Long postId;
    private Location location;
    private String description;
    private Integer numberOfLikes;
    private Integer numberOfComments;
//    @JsonIgnore
    private List<Photo> photos;
    @JsonIgnore
    private List<Video> videos;
}