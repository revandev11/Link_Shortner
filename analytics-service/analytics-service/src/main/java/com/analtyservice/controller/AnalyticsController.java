package com.analtyservice.controller;

import com.analtyservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/api/analytics/{shortCode}")
    public Map<String, Object> getStats(@PathVariable String shortCode) {
        return analyticsService.getStats(shortCode);
    }
}
