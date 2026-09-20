package com.example.urlservice.service;


import com.example.urlservice.config.RabbitMqConfig;
import com.example.urlservice.event.UrlEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCreated(String shortCode, String originalUrl) {
        publish("url.created", new UrlEvent(shortCode, originalUrl, "CREATED", LocalDateTime.now()));
    }

    public void publishClicked(String shortCode, String originalUrl) {
        publish("url.clicked", new UrlEvent(shortCode, originalUrl, "CLICKED", LocalDateTime.now()));
    }

    private void publish(String routingKey, UrlEvent event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.URL_EVENTS_EXCHANGE, routingKey, event);
        } catch (Exception ex) {
            log.warn("Hadisə göndərilə bilmədi: {}", ex.getMessage());
        }
    }
}
