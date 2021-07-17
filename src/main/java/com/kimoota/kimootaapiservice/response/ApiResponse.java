package com.kimoota.kimootaapiservice.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse {

    private boolean success;
    private String message;

    @Builder
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
