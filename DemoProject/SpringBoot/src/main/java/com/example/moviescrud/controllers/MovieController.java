package com.example.moviescrud.controllers;

import com.example.moviescrud.models.Movie;
import com.example.moviescrud.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movies")
    @ResponseBody
    public List<Movie> getAllMovies(){
        return movieService.getMovies();
    }

    @PostMapping("/save_movie")
    @ResponseBody
    public String saveMovie(@RequestBody Movie movie){
        movieService.saveMovie(movie);
        return "Movie saved";
    }

    @PutMapping("/update_movie")
    @ResponseBody
    public String updateMovie(@RequestBody Movie movie){
        movieService.saveMovie(movie);
        return "Movie updated";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
        return "Deleted movie";
    }
}
