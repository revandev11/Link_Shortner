package com.example.urlservice.event;


import java.io.Serializable;
import java.time.LocalDateTime;

public record UrlEvent(

        String shortCode,
        String originalUrl,
        String eventType,
        LocalDateTime timestamp

) implements Serializable {}
