package com.tmp.authentication.authorization.jwt.services;

import com.lowagie.text.DocumentException;
import com.tmp.authentication.authorization.jwt.entities.Certificate;
import com.tmp.authentication.authorization.jwt.exceptions.StorageException;
import com.tmp.authentication.authorization.jwt.models.CertificateDTO;
import com.tmp.authentication.authorization.jwt.models.CreateCertificateDTO;
import com.tmp.authentication.authorization.jwt.models.adapters.CertificateAdapter;
import com.tmp.authentication.authorization.jwt.repositories.CertificateRepository;
import com.tmp.authentication.authorization.jwt.repositories.EmployeesCertificateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final EmployeesCertificateRepository employeesCertificateRepository;

    private final SpringTemplateEngine springTemplateEngine;

    @Value("${certificates.path}")
    private String certificatesPath;

    @Value("${api.path}")
    private String apiPath;

    /*
        CertificateService methods
     */
    @PostConstruct
    private void init() {
        try {
            Files.createDirectories(Paths.get(certificatesPath));
        } catch (IOException ioException) {
            throw new StorageException(String.format("Could not initialize storage (%s).", ioException.getMessage()));
        }
    }

    private void save(MultipartFile file) {
        Path root = Paths.get(certificatesPath);
        if (!Files.exists(root)) {
            init();
        }

        try {
            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
        } catch (IOException ioException) {
            throw new StorageException(String.format("Failed to store file %s (%s).", file.getOriginalFilename(),
                    ioException.getMessage()));
        }
    }

    private void checkEmptyFiles(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }
    }

    /*
        Methods from CertificateController
     */
    public CertificateDTO createCertificateDTO(CreateCertificateDTO createCertificateDTO, String template) {
        Certificate certificate = Certificate.builder()
                .name("certificate-1")
                .path("path-1")
                .build();

        Certificate savedCertificate = certificateRepository.save(certificate);

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeFirstName", createCertificateDTO.getEmployeeFirstName());
        variables.put("employeeLastName", createCertificateDTO.getEmployeeLastName());
        variables.put("employeeNumber", createCertificateDTO.getEmployeeNumber());
        variables.put("courseName", createCertificateDTO.getCourseName());
        variables.put("courseCategory", createCertificateDTO.getCourseCategory());
        variables.put("courseCompletedDate", savedCertificate.getReleaseDate());

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        String certificateName = savedCertificate.getName() + ".pdf";

        // Convert HTML to PDF and save in FileSystem
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(certificatesPath + "/" + certificateName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
        } catch (FileNotFoundException | DocumentException exception) {
            throw new StorageException(
                    String.format("File not found on storage certificate or error on create pdf (%s).",
                            exception.getMessage()));
        }

        return CertificateAdapter.certificateToCertificateDTO(savedCertificate);
    }
}
