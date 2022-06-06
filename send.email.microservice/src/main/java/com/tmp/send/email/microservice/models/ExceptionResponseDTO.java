package com.tmp.send.email.microservice.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ExceptionResponseDTO {

    private String status;
    private String message;

    public ExceptionResponseDTO(String status, String message) {
        this.status = "error";
        this.message = message;
    }
}