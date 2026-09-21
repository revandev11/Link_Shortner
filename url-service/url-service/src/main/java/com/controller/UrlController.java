package com.example.urlservice.controller;

import com.example.urlservice.dto.ShortenRequest;
import com.example.urlservice.dto.ShortenResponse;
import com.example.urlservice.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "URLs", description = "URL qısaltma və yönləndirmə əməliyyatları")
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/urls")
    @Operation(summary = "URL qısalt", description = "Verilən tam URL üçün unikal qısa kod yaradır.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Qısa URL uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "URL etibarsızdır")
    })
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request,
                                                     HttpServletRequest httpRequest) {
        String baseUrl = httpRequest.getScheme() + "://" + httpRequest.getServerName()
                + ":" + httpRequest.getServerPort();

        ShortenResponse response = urlService.shorten(request.url(), baseUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Orijinal URL-ə yönləndir", description = "Qısa kodu həll edib istifadəçini orijinal URL-ə yönləndirir.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "Orijinal URL-ə yönləndirildi"),
            @ApiResponse(responseCode = "404", description = "Qısa kod tapılmadı")
    })
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.resolve(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
