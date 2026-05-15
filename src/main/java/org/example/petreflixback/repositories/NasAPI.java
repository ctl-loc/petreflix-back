package org.example.petreflixback.repositories;

import io.vavr.control.Either;
import jakarta.annotation.PostConstruct;
import org.apache.hc.client5.http.impl.ConnectionShutdownException;
import org.example.petreflixback.model.Movie;
import org.example.petreflixback.model.Series;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Repository
public class NasAPI {

    @Value("${nas.address}")
    private String API_ADDR;
    @Value("${nas.port}")
    private String API_PORT;

    Environment env;
    private final RestClient client;

    private String token;
    private String sid;

    private static final List<String> excludedPaths = List.of(
            "/Films/#recycle"
    );


    public NasAPI(RestTemplate restTemplate, Environment env) {
        this.env = env;
        client = RestClient.create(restTemplate);
    }

    @PostConstruct
    private void login() throws ConnectionShutdownException {
        // Request set up
        Map<String, List<String>> params = new HashMap<>();
        params.put("api", List.of("SYNO.API.Auth"));
        params.put("version", List.of("6"));
        params.put("method", List.of("login"));
        params.put("account", List.of(Objects.requireNonNull(env.getProperty("nas.username"))));
        params.put("passwd", List.of(Objects.requireNonNull(env.getProperty("nas.password"))));
        params.put("session", List.of("FileStation"));
        params.put("enable_syno_token", List.of("yes"));
        params.put("format", List.of("sid"));

        // Execute request
        ResponseEntity<String> result = client.get().uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(API_ADDR)
                        .port(API_PORT)
                        .path("/webapi/auth.cgi")
                        .queryParams(CollectionUtils.toMultiValueMap(params))
                        .build())
                .retrieve()
                .toEntity(String.class);

        // Parse response
        JSONObject response = new JSONObject(result.getBody());

        if (!response.getBoolean("success")) {
            Logger.getGlobal().severe("Login failed: %s".formatted(response.toString()));
            throw new ConnectionShutdownException();
        }

        JSONObject data = response.getJSONObject("data");
        if (data.isEmpty() || data.get("sid") == null || data.get("synotoken") == null) {
            Logger.getGlobal().severe("Login response unproperly formatted: %s".formatted(response.toString()));
            throw new ConnectionShutdownException();
        }

        this.token = data.get("synotoken").toString();
        this.sid = data.get("sid").toString();
    }

    public Either<Movie, Series>[] fetch(boolean movie, boolean series) {
        return fetchByPath("/Films").stream().<Either<Movie, Series>>map(Either::left).toArray(n -> (Either<Movie, Series>[]) new Either[n]);
    }

    public Either<Movie, Series>[] fetch() {
        return fetch(true, true);
    }

    private List<Movie> fetchByPath(String path) throws RestClientException {
        // Request set up
        Map<String, List<String>> params = new HashMap<>();
        params.put("api", List.of("SYNO.FileStation.List"));
        params.put("version", List.of("2"));
        params.put("method", List.of("list"));
        params.put("folder_path", List.of(Objects.requireNonNull(path)));
        params.put("_sid", List.of(this.sid));
        params.put("SynoToken", List.of(this.token));

        Logger.getGlobal().info(params.toString());

        // Execute request
        ResponseEntity<String> result = client.get().uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(API_ADDR)
                        .port(API_PORT)
                        .path("/webapi/entry.cgi")
                        .queryParams(CollectionUtils.toMultiValueMap(params))
                        .build())
                .retrieve()
                .toEntity(String.class);

        // Parse response
        JSONObject response = new JSONObject(result.getBody());

        if (!response.getBoolean("success")) {
            Logger.getGlobal().info(response.toString());
            throw new ConnectionShutdownException();
        }

        JSONArray data = response.getJSONObject("data").getJSONArray("files");
        List<Movie> movies = new ArrayList<>();


        for (Object obj : data) {
            JSONObject element = (JSONObject) obj;

            // Recursive call for dir
            if (element.getBoolean("isdir")) {
                String nextPath = element.getString("path");
                if (excludedPaths.contains(nextPath))
                    continue;
                movies.addAll(fetchByPath(element.getString("path")));
            }
            // Add item to list
            else {
                movies.addLast(new Movie(element));
            }
        }

        return movies;
    }
}
