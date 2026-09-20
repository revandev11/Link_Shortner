package com.analtyservice.service;

import com.analtyservice.dto.UrlEvent;
import com.analtyservice.entity.AnalyticsEvent;
import com.analtyservice.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsEventRepository repository;

    public void saveEvent(UrlEvent event) {
        AnalyticsEvent entity = new AnalyticsEvent();
        entity.setShortCode(event.shortCode());
        entity.setOriginalUrl(event.originalUrl());
        entity.setEventType(event.eventType());
        entity.setEventTimestamp(event.timestamp());
        repository.save(entity);

        log.info(":The event was recorded shortCode={}, type={}", event.shortCode(), event.eventType());
    }

    public Map<String, Object> getStats(String shortCode) {
        long clicks = repository.countByShortCodeAndEventType(shortCode, "CLICKED");
        long created = repository.countByShortCodeAndEventType(shortCode, "CREATED");

        return Map.of(
                "shortCode", shortCode,
                "totalClicks", clicks,
                "createdEvents", created
        );
    }
}
