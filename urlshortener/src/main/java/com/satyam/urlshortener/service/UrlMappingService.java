package com.satyam.urlshortener.service;

import com.satyam.urlshortener.entity.UrlMapping;
import com.satyam.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDateTime;
// import java.util.Optional;

@Service
public class UrlMappingService {

    private final UrlMappingRepository repository;

    public UrlMappingService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    // Create a short URL with optional expiry
    // @CachePut(value = "shortUrls", key = "#result.shortUrl")
    public UrlMapping createShortUrl(String originalUrl, LocalDateTime expiresAt) {

        // 1️⃣ Set default expiry if not provided
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusDays(2); // default 10 days
        }

        if (originalUrl == null || originalUrl.isBlank()) {
            throw new IllegalArgumentException("Original URL must not be null or empty");
        }
        String code;
        do {
            code = generateShortCode();
        } while (repository.findByShortUrl(code).isPresent());

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortUrl(code);
        mapping.setExpiresAt(expiresAt);

        return repository.save(mapping);
    }

    // Get original URL from cache or DB, increment click count
    @Cacheable(value = "shortUrls", key = "#shortUrl")
    @Transactional
    public String getOriginalUrl(String shortUrl) {
        UrlMapping mapping = repository.findByShortUrl(shortUrl)
                .filter(m -> m.getExpiresAt() == null ||
                        m.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(null);

        if (mapping == null) {
            return null;
        }

        mapping.setClickCount(mapping.getClickCount() + 1);
        return mapping.getOriginalUrl();
    }

    // Optional: Evict URL from cache
    @CacheEvict(value = "shortUrls", key = "#shortUrl")
    public void deleteShortUrl(String shortUrl) {
        repository.findByShortUrl(shortUrl).ifPresent(repository::delete);
    }

    private String generateShortCode() {
        return Long.toHexString(System.currentTimeMillis());
    }
}
