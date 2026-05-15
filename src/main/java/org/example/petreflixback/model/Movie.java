package org.example.petreflixback.model;

import lombok.Data;
import org.json.JSONObject;

import java.util.UUID;

@Data
public class Movie {
    @Data
    public static class MovieInfo {
        private boolean adult;
        private String backdrop_path;
        private int[] genre_ids;
        private UUID id;
        private String original_language;
        private String original_title;
        private String overview;
        private int popularity;
        private String poster_path;
        private String release_date;
        private String title;
        boolean video;
        private int vote_average;
        private int vote_count;
    }

    public Movie(JSONObject json) {
        id = UUID.randomUUID();
        title = json.get("name").toString();
        path = json.get("path").toString();
    }

    private UUID id;
    private String title;
    private String path;
    private MovieInfo info;
    private String[] genre;
}
