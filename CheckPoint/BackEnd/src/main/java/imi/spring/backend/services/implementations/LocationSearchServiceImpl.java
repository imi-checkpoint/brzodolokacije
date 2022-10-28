package imi.spring.backend.services.implementations;

import imi.spring.backend.models.Location;
import imi.spring.backend.models.LocationSearch;
import imi.spring.backend.repositories.LocationRepository;
import imi.spring.backend.repositories.LocationSearchRepository;
import imi.spring.backend.services.LocationSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LocationSearchServiceImpl implements LocationSearchService {

    private final LocationSearchRepository locationSearchRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<LocationSearch> getLocationSearches() {
        return locationSearchRepository.findAll();
    }

    @Override
    public LocationSearch getLocationSearchById(Long id) {
        return locationSearchRepository.findById(id).get();
    }

    @Override
    public LocationSearch saveSearchForLocation(Long locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isPresent()) {
            LocationSearch locationSearch = new LocationSearch(1L, locationOptional.get(), LocalDateTime.now());
            return locationSearchRepository.save(locationSearch);
        }
        else {
            System.out.println("LOCATION NOT FOUND:" + locationOptional);
            return null;
        }
    }

}
