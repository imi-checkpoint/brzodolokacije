package imi.spring.backend.services.implementations;

import imi.spring.backend.models.Location;
import imi.spring.backend.models.LocationSearch;
import imi.spring.backend.repositories.LocationSearchRepository;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.LocationSearchService;
import imi.spring.backend.services.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LocationSearchServiceImpl implements LocationSearchService {

    private final LocationSearchRepository locationSearchRepository;
    private final LocationService locationService;
    private final AppUserService appUserService;

    @Override
    public List<LocationSearch> getLocationSearches() {
        return locationSearchRepository.findAll();
    }

    @Override
    public LocationSearch getLocationSearchById(Long id) {
        Optional<LocationSearch> locationSearch = locationSearchRepository.findById(id);
        if (locationSearch.isPresent())
            return locationSearch.get();
        return null;
    }

    @Override
    public String saveSearchForLocation(Long locationId) {
        Location location = locationService.getLocationById(locationId);
        if (location != null) {
            locationSearchRepository.save(new LocationSearch(appUserService.getAllUsers().get(0), location, LocalDateTime.now()));
            return "location search has been saved successfully";
        }
        return"location with that id does not exist";
    }

}
