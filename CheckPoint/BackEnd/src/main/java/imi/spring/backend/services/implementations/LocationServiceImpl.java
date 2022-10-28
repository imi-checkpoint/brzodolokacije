package imi.spring.backend.services.implementations;

import imi.spring.backend.models.Location;
import imi.spring.backend.repositories.LocationRepository;
import imi.spring.backend.repositories.LocationSearchRepository;
import imi.spring.backend.services.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public List<Location> getLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }
}
