package com.example.moviescrud.services;

import com.example.moviescrud.models.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getMovies();
    void saveMovie(Movie movie);
    void updateMovie(Movie movie);
    void deleteMovie(Long id);
}