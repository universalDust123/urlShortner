package com.satyam.urlshortener.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.satyam.urlshortener.repository.UrlMappingRepository;

import java.time.LocalDateTime;

@Service
public class ExpiredUrlCleanupService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExpiredUrlCleanupService.class);
    private final UrlMappingRepository repository;

    public ExpiredUrlCleanupService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    // Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteExpiredUrls() {
        logger.info("Starting expired URL cleanup job...");
        int deletedCount = repository.deleteByExpiresAtBefore(LocalDateTime.now());
        logger.info("Deleted expired URLs count: {}", deletedCount);
    }
}
