package org.example.petreflixback.model;

import lombok.Data;

@Data
public class Series {
    @Data
    public static class SeriesEpisode {
        private int episode;
        private String path;
        private int season;
    }

    @Data
    public static class SeriesInfo {
        private boolean adult;
        private String backdrop_path;
        private String first_air_date;
        private int[] genre_ids;
        private int id;
        private String name;
        private String[] origin_country;
        private String original_language;
        private String original_name;
        private String overview;
        private int popularity;
        private String poster_path;
        private int vote_average;
        private int vote_count;
    }

    private int id;
    private String title;
    private String path;
    private SeriesEpisode[] episodes;
    private SeriesInfo info;
    private String[] genres;
    private int seasons_count;

}
