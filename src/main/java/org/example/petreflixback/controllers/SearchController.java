package org.example.petreflixback.controllers;

import io.vavr.control.Either;
import org.example.petreflixback.model.Movie;
import org.example.petreflixback.model.Series;
import org.example.petreflixback.services.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class SearchController {

    private final SearchService search;

    public SearchController(SearchService search) {
        this.search = search;
    }

    public ResponseEntity<Either<Movie, Series>> search(@RequestParam String query, @RequestParam(required = false) String type) {
        if (type != null &&
                (type.compareTo("movie") != 0 || type.compareTo("series") != 0))
            // type specified and invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return search.execute(query);
    }
}
