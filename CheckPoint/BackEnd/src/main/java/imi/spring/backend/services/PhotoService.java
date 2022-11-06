package imi.spring.backend.services;

import imi.spring.backend.models.mongo.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    String addPhoto(Long postId, MultipartFile file) throws IOException;
    List<Photo> getPhotos(Long postId);
    List<String> getEncodedPhotos(Long postId);
}
