package com.example.urlservice.dto;

public record ShortenResponse(

        String originalUrl,
        String shortCode,
        String shortUrl

) {}
