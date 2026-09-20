package com.example.urlservice.service;


import com.example.urlservice.dto.ShortenResponse;
import com.example.urlservice.entity.UrlMapping;
import com.example.urlservice.exception.UrlNotFoundException;
import com.example.urlservice.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UrlService {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 7;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UrlRepository urlRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public ShortenResponse shorten(String originalUrl, String baseUrl) {
        String shortCode = generateUniqueCode();

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(shortCode);
        urlRepository.save(mapping);

        eventPublisher.publishCreated(shortCode, originalUrl);

        String shortUrl = baseUrl + "/" + shortCode;
        return new ShortenResponse(originalUrl, shortCode, shortUrl);
    }

    @Transactional
    public String resolve(String shortCode) {
        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        mapping.setClickCount(mapping.getClickCount() + 1);
        urlRepository.save(mapping);

        eventPublisher.publishClicked(shortCode, mapping.getOriginalUrl());

        return mapping.getOriginalUrl();
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = randomCode();
        } while (urlRepository.existsByShortCode(code));
        return code;
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
