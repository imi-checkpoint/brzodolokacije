package imi.spring.backend.models;

import imi.spring.backend.models.mongo.Photo;
import imi.spring.backend.models.mongo.Video;
import lombok.Data;

import java.util.List;

@Data
public class PostDTO {

    private Long appUserId;
    private String appUserUsername;

    private Location location;
    private String description;
    private Integer numberOfLikes;
    private List<Photo> photos;
    private List<Video> videos;
}