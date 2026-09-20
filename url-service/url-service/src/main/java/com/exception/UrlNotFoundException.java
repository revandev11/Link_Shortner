package com.example.urlservice.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String shortCode) {
        super("Bu koda uyğun URL tapılmadı: " + shortCode);
    }
}
