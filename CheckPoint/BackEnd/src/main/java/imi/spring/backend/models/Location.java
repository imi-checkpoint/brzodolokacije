package imi.spring.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String coordinates; //or latitude and longitude ?

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private List<LocationSearch> locationSearchList = new ArrayList<LocationSearch>();

    public Location() {

    }
    public Location(String name, String coordinates, List<LocationSearch> locationSearchList) {
        this.name = name;
        this.coordinates = coordinates;
        this.locationSearchList = locationSearchList;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCoordinates() { return coordinates; }

    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    public List<LocationSearch> getLocationSearchList() { return locationSearchList; }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates='" + coordinates + '\'' +
                '}';
    }
}
