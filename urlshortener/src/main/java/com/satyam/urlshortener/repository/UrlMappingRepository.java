package com.satyam.urlshortener.repository;

import com.satyam.urlshortener.entity.UrlMapping;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);
    
     @Transactional
    int deleteByExpiresAtBefore(LocalDateTime time);
}
