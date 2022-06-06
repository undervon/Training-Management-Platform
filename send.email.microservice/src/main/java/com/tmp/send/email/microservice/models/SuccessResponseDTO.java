package com.tmp.send.email.microservice.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class SuccessResponseDTO<T> {

    private String status;
    private T data;

    public SuccessResponseDTO(String status, T data) {
        this.status = "success";
        this.data = data;
    }
}