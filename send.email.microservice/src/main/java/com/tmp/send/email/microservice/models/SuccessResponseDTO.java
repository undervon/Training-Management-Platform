package com.tmp.send.email.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDTO<T> {

    @Builder.Default
    private String status = "success";
    private T data;
}