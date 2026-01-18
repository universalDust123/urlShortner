package com.satyam.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;

public class UrlRequestDto {
    @NotBlank(message = "Original URL must not be empty")
    @URL(message = "Invalid URL format")
    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "URL must start with http:// or https://"
    )
    private String originalUrl;

    // OPTIONAL
    private LocalDateTime expiresAt;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

     public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
