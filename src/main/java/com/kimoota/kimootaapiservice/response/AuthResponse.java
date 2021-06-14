package com.kimoota.kimootaapiservice.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthResponse {

    private String token;
    private String type = "Bearer";

    @Builder
    public AuthResponse(String token) {
        this.token = token;
    }
}
