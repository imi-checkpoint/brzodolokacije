package com.example.moviescrud.services.implementations;

import com.example.moviescrud.models.Movie;
import com.example.moviescrud.repositories.MovieRepository;
import com.example.moviescrud.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(Movie movie) {
        Movie movieToEdit = movieRepository.findById(movie.getId())
                .orElseThrow(() -> new IllegalStateException("movie does not exist"));

        if (movie.getName() != null && movie.getName().length() > 0 && !Objects.equals(movieToEdit.getName(), movie.getName())) {
            movieToEdit.setName(movie.getName());
        }
        if (movie.getGenre() != null && movie.getGenre().length() > 0 && !Objects.equals(movieToEdit.getGenre(), movie.getGenre())) {
            movieToEdit.setGenre(movie.getGenre());
        }
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}