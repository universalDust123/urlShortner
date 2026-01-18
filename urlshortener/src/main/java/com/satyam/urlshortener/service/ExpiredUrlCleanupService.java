package com.satyam.urlshortener.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.satyam.urlshortener.repository.UrlMappingRepository;

import java.time.LocalDateTime;

@Service
public class ExpiredUrlCleanupService {

    private final UrlMappingRepository repository;

    public ExpiredUrlCleanupService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    // Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteExpiredUrls() {
        int deletedCount = repository.deleteByExpiresAtBefore(LocalDateTime.now());
        System.out.println("Deleted expired URLs: " + deletedCount);
    }
}
