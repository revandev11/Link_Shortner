package com.analtyservice.listener;


import com.analtyservice.config.RabbitMQConfig;
import com.analtyservice.dto.UrlEvent;
import com.analtyservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UrlEventListener {

    private final AnalyticsService analyticsService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleUrlEvent(UrlEvent event) {
        log.info("the event received: {}", event);
        analyticsService.saveEvent(event);
    }
}