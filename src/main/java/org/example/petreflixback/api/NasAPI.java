package org.example.petreflixback.api;

import jakarta.annotation.PostConstruct;
import org.apache.hc.client5.http.impl.ConnectionShutdownException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Repository
public class NasAPI {

    @Value("${nas.address}")
    private String API_ADDR;
    @Value("${nas.port}")
    private String API_PORT;
    @Autowired
    Environment env;

    private String token;

    private final RestClient client;


    public NasAPI(RestTemplate restTemplate) {
        client = RestClient.create(restTemplate);
    }

    @PostConstruct
    public void login() {
        // Request set up
        Map<String, List<String>> params = new HashMap<>();
        params.put("api", List.of("SYNO.API.Auth"));
        params.put("version",  List.of("6"));
        params.put("method",  List.of("login"));
        params.put("account",  List.of(Objects.requireNonNull(env.getProperty("nas.username"))));
        params.put("passwd",  List.of(Objects.requireNonNull(env.getProperty("nas.password"))));
        params.put("enable_syno_token",  List.of("yes"));

        // Execute request
        ResponseEntity<String> result = client.get().uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host(API_ADDR)
                .port(API_PORT)
                .path("/webapi/auth.cgi")
                .queryParams(CollectionUtils.toMultiValueMap(params))
                .build()
        )
                .retrieve()
                .toEntity(String.class);

        // Parse response
        JSONObject response = new JSONObject(result.getBody());
        if (!response.getBoolean("success")) {
            throw new ConnectionShutdownException();
        }

        if (response.getJSONObject("data") == null
        || response.getJSONObject("data").get("sid") == null) {
            throw new ConnectionShutdownException();
        }

        this.token = response.getJSONObject("data").get("sid").toString();

        Logger.getGlobal().info(token);

    }


}
