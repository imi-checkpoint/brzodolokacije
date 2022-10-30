package imi.spring.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    @ManyToOne()
    @JoinColumn(name = "id_location", referencedColumnName = "id")
    private Location location;
    @ManyToOne()
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private AppUser user;

    public LocationSearch(AppUser user, Location location, LocalDateTime dateTime) {
        this.user = user;
        this.location = location;
        this.time = dateTime;
    }
}
