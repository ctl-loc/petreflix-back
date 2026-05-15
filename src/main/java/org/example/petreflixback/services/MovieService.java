package org.example.petreflixback.services;

import io.vavr.control.Either;
import org.example.petreflixback.Query;
import org.example.petreflixback.model.Movie;
import org.example.petreflixback.repositories.NasAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MovieService implements Query<Integer, Movie[]> {

    private final NasAPI api;

    public MovieService(NasAPI api) {
        this.api = api;
    }

    @Override
    public ResponseEntity<Movie[]> execute(Integer input) {
        return ResponseEntity.ok(Arrays.stream(api.fetch()).map(Either::getLeft).toArray(Movie[]::new));
    }
}
