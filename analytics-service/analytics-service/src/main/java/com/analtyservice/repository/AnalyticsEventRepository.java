package com.analtyservice.repository;

import com.analtyservice.entity.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    List<AnalyticsEvent> findByShortCode(String shortCode);

    long countByShortCodeAndEventType(String shortCode, String eventType);
}