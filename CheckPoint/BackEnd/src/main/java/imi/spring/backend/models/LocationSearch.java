package imi.spring.backend.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class LocationSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long id_user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    private LocalDateTime time;


    public LocationSearch() {

    }
    public LocationSearch(Long id_user, Location location, LocalDateTime time) {
        this.id_user = id_user;
        this.location = location;
        this.time = time;
    }

    public Long getId() { return id; }

    public Long getId_user() { return id_user; }

    public void setId_user(Long id_user) { this.id_user = id_user; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public LocalDateTime getTime() { return time; }

    public void setTime(LocalDateTime time) { this.time = time; }
}
