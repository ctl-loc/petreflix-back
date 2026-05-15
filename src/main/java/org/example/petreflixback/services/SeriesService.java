package org.example.petreflixback.services;

import org.example.petreflixback.Query;
import org.example.petreflixback.model.Series;
import org.springframework.http.ResponseEntity;

public class SeriesService implements Query<Integer, Series> {
    @Override
    public ResponseEntity<Series> execute(Integer input) {
        return null;
    }
}
