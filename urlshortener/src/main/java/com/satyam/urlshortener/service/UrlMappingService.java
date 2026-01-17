package com.satyam.urlshortener.service;

import com.satyam.urlshortener.entity.UrlMapping;
import com.satyam.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlMappingService {

    private final UrlMappingRepository repository;
    private final Random random = new Random();

    public UrlMappingService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    // Generate a short URL code
    private String generateShortUrl() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortUrl = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            shortUrl.append(chars.charAt(random.nextInt(chars.length())));
        }
        return shortUrl.toString();
    }

    // Create a short URL
    public UrlMapping createShortUrl(String originalUrl) {
        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (repository.findByShortUrl(shortUrl).isPresent());

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortUrl(shortUrl);
        return repository.save(mapping);
    }

    // Retrieve original URL
    public Optional<UrlMapping> getOriginalUrl(String shortUrl) {
        return repository.findByShortUrl(shortUrl);
    }
}
