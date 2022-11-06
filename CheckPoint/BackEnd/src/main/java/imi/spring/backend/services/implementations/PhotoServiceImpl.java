package imi.spring.backend.services.implementations;

import imi.spring.backend.models.mongo.Photo;
import imi.spring.backend.repositories.mongo.PhotoRepository;
import imi.spring.backend.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    public String addPhoto(Long postId, MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setPostId(postId);
        photo.setPhoto(
                new Binary(BsonBinarySubType.BINARY, file.getBytes())
        );
        photo = photoRepository.insert(photo);
        return photo.getId();
    }

    @Override
    public List<Photo> getPhotos(Long postId) {
        List<Photo> photos = photoRepository.getPhotosByPostId(postId);
        if(photos!=null && photos.size()>0){
            return photoRepository.getPhotosByPostId(postId);
        }
        return null;
    }

    @Override
    public List<String> getEncodedPhotos(Long postId) {
        List<Photo> photos = getPhotos(postId);
        List<String> encodedPhotos = new ArrayList<>();

        for(Photo photo : photos){
            encodedPhotos.add(Base64.getEncoder().encodeToString(photo.getPhoto().getData()));
        }

        return encodedPhotos;
    }
}
