package com.tmp.authentication.authorization.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private LocalDateTime releaseDate = LocalDateTime.now();

    @NotNull
    private Integer availability = 12;

    @NotNull
    @NotBlank
    private String path;
}
