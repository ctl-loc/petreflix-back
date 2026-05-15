package org.example.petreflixback.services;

import org.example.petreflixback.interfaces.Query;
import org.example.petreflixback.types.MovieOrSeries;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements Query<String, MovieOrSeries> {
    public ResponseEntity<MovieOrSeries> execute(String input) {
        throw new UnsupportedOperationException("SearchService::execute is not supported yet");
    }
}
