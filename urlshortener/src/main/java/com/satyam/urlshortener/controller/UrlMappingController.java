package com.satyam.urlshortener.controller;

import com.satyam.urlshortener.dto.UrlRequestDto;
import com.satyam.urlshortener.dto.UrlResponseDto;
import com.satyam.urlshortener.entity.UrlMapping;
import com.satyam.urlshortener.service.UrlMappingService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<UrlResponseDto> shortenUrl(
            @Valid @RequestBody UrlRequestDto request) {

        UrlMapping mapping = service.createShortUrl(request.getOriginalUrl(), request.getExpiresAt());

        if (mapping == null) {
            return ResponseEntity.status(500).body(new UrlResponseDto("Failed to create short URL"));
        }

        return ResponseEntity.ok(
                new UrlResponseDto("http://localhost:9090/r/" + mapping.getShortUrl()));
    }

    // Redirect to original URL
    @GetMapping("/r/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortUrl)
            throws IOException {
        return service.getOriginalUrl(shortUrl)
                .map(mapping -> ResponseEntity.status(302)
                        .header("Location", mapping.getOriginalUrl())
                        .build())
                .orElse(ResponseEntity.status(410).body("Link expired or not found"));
    }
}
