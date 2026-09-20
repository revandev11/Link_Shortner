package com.example.urlservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShortenRequest(

        @NotBlank(message = "URL boş ola bilməz")
        @Pattern(regexp = "^https?://.*", message = "URL http:// və ya https:// ilə başlamalıdır")
        String url

) {}
