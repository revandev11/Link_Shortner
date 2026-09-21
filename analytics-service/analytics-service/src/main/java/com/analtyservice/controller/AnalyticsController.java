package com.analtyservice.controller;

import com.analtyservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Qısa URL istifadə statistikası")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/api/analytics/{shortCode}")
    @Operation(summary = "URL statistikasını göstər", description = "Qısa kod üzrə yaradılma və kliklənmə saylarını qaytarır.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistika uğurla qaytarıldı")
    })
    public Map<String, Object> getStats(@PathVariable String shortCode) {
        return analyticsService.getStats(shortCode);
    }
}
