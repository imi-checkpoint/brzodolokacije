package imi.spring.backend.controllers;

import imi.spring.backend.models.Location;
import imi.spring.backend.models.LocationSearch;
import imi.spring.backend.services.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/location")
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/all")
    @ResponseBody
    public List<Location> getAllLocations() { return locationService.getLocations(); }

    @PostMapping("/save")
    @ResponseBody
    public Location saveLocation(@RequestBody Location location) { return locationService.saveLocation(location); }
}
