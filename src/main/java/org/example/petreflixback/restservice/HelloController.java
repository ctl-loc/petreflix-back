package org.example.petreflixback.restservice;

import org.example.petreflixback.api.NasAPI;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class HelloController {

    private static final String template =  "Hello %s";
    private final AtomicLong counter = new AtomicLong(0);
    private final NasAPI api;

    public HelloController(NasAPI api) {
        this.api = api;
    }

    @GetMapping("/hello")
    public Hello hello(@RequestParam(defaultValue = "World") String name) {
        return new Hello(counter.incrementAndGet(), template.formatted(name));
    }
}
