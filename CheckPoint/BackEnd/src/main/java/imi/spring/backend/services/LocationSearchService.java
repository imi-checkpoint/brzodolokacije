package imi.spring.backend.services;

import imi.spring.backend.models.Location;
import imi.spring.backend.models.LocationSearch;

import java.util.List;

public interface LocationSearchService {


    List<LocationSearch> getLocationSearches();

    LocationSearch getLocationSearchById(Long id);

    String saveSearchForLocation(Long locationId);
}
