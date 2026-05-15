package org.example.petreflixback.interfaces;

import org.example.petreflixback.types.MovieOrSeries;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface NasApiInterface {

    /**
     * @param movies True if fetching the movies
     * @param series True if fetching the series
     * @return The list of MovieOrSeries found
     */
    MovieOrSeries[] fetch(boolean movies, boolean series);


    /**
     * WIP
     *
     * @param id
     * @return
     */
    Flux<DataBuffer> download(UUID id);
}
