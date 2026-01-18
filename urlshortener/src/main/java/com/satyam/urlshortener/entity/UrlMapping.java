package com.satyam.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "url_mapping")
public class UrlMapping implements Serializable {

     private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false)
    private Long clickCount = 0L;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;
}
