package com.analtyservice.dto;


import java.time.LocalDateTime;

public record UrlEvent(
        String shortCode,
        String originalUrl,
        String eventType,   // "CREATED" və ya "CLICKED"
        LocalDateTime timestamp

) {}
