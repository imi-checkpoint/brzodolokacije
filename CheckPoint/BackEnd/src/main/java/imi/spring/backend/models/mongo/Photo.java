package imi.spring.backend.models.mongo;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@Document(collection = "photos")
public class Photo {
    @Id
    private String id;
    private Long postId;
    private Binary photo;
}