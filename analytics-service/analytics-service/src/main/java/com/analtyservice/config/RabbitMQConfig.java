package com.analtyservice.config;


import com.analtyservice.dto.UrlEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    // DİQQƏT: bu ad url-service-in RabbitMQConfig-indəki URL_EVENTS_EXCHANGE ilə
    // AYNI olmalıdır — əks halda url-service-in göndərdiyi mesajlar bura çatmır.
    public static final String EXCHANGE = "url.events";
    public static final String QUEUE = "analytics.queue";
    // "url.*" hər iki routing key-i tutur: "url.created" və "url.clicked"
    public static final String ROUTING_PATTERN = "url.*";

    @Bean
    public TopicExchange urlEventsExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue analyticsQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue analyticsQueue, TopicExchange urlEventsExchange) {
        return BindingBuilder.bind(analyticsQueue).to(urlEventsExchange).with(ROUTING_PATTERN);
    }
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(Map.of(
                "com.example.urlservice.event.UrlEvent", UrlEvent.class
        ));
        classMapper.setTrustedPackages("*");
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}