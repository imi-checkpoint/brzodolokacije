package com.example.moviescrud.repositories;

import com.example.moviescrud.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
}
