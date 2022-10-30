package imi.spring.backend.controllers;

import imi.spring.backend.models.Location;
import imi.spring.backend.models.LocationSearch;
import imi.spring.backend.services.LocationSearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/location_searches")
public class LocationSearchController {

    private final LocationSearchService locationSearchService;

    @GetMapping("/all")
    @ResponseBody
    public List<LocationSearch> getAllLocationSearches() { return locationSearchService.getLocationSearches(); }

    @GetMapping("/{id}")
    @ResponseBody
    public LocationSearch getLocationSearchById(@PathVariable Long id) { return locationSearchService.getLocationSearchById(id); }

    @PostMapping("/save_search/{locationId}")
    @ResponseBody
    String saveLocationSearch(@PathVariable Long locationId) {
        return locationSearchService.saveSearchForLocation(locationId);
    }

}
