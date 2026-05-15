package org.example.petreflixback.controllers;

import org.example.petreflixback.model.Movie;
import org.example.petreflixback.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {

    public final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/movie")
    public ResponseEntity<Movie[]> searchMovies() {
        return service.execute(0);
    }
}
