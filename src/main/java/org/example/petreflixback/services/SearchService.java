package org.example.petreflixback.services;

import io.vavr.control.Either;
import org.example.petreflixback.Query;
import org.example.petreflixback.model.Movie;
import org.example.petreflixback.model.Series;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements Query<String, Either<Movie, Series>> {
    public ResponseEntity<Either<Movie, Series>> execute(String input) {
        throw new UnsupportedOperationException("SearchService::execute is not supported yet");
    }
}
