package com.satyam.urlshortener.controller;

import com.satyam.urlshortener.entity.UrlMapping;
import com.satyam.urlshortener.service.UrlMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class UrlMappingController {

    private final UrlMappingService service;

    public UrlMappingController(UrlMappingService service) {
        this.service = service;
    }

    // Create short URL
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestParam String originalUrl) {
        UrlMapping mapping = service.createShortUrl(originalUrl);
        return ResponseEntity.ok("Short URL: http://localhost:9090/r/" + mapping.getShortUrl());
    }

    // Redirect to original URL
    @GetMapping("/r/{shortUrl}")
    public void redirectToOriginal(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        service.getOriginalUrl(shortUrl).ifPresentOrElse(
                mapping -> {
                    try {
                        response.sendRedirect(mapping.getOriginalUrl());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
