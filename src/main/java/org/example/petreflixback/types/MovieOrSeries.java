package org.example.petreflixback.types;

import io.vavr.control.Either;
import org.example.petreflixback.model.Movie;
import org.example.petreflixback.model.Series;

public class MovieOrSeries {
    private final Either<Movie, Series> value;

    private MovieOrSeries(Either<Movie, Series> value) {
        this.value = value;
    }


    public static MovieOrSeries movie(Movie m) {
        return new MovieOrSeries(Either.left(m));
    }

    public static MovieOrSeries series(Series s) {
        return new MovieOrSeries(Either.right(s));
    }

    public Either<Movie, Series> get() {
        return value;
    }

    public Movie left() {
        return value.getLeft();
    }

    public Series right() {
        return value.get();
    }
}
