package com.tmp.authentication.authorization.jwt.models.adapters;

import com.tmp.authentication.authorization.jwt.entities.Certificate;
import com.tmp.authentication.authorization.jwt.models.CertificateDTO;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class CertificateAdapter {

    public static CertificateDTO certificateToCertificateDTO(Certificate certificate) {
        return CertificateDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .releaseDate(certificate.getReleaseDate())
                .availability(certificate.getAvailability())
                .path(certificate.getPath())
                .build();
    }

    public static List<CertificateDTO> certificateListToCertificateDTOList(List<Certificate> certificateList) {
        return certificateList.stream()
                .map(CertificateAdapter::certificateToCertificateDTO)
                .collect(Collectors.toList());
    }
}
