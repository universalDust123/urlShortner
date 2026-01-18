package com.satyam.urlshortener.dto;

public class UrlResponseDto {
    private String shortUrl;

    public UrlResponseDto(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
