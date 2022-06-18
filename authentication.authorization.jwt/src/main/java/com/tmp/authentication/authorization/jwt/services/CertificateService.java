package com.tmp.authentication.authorization.jwt.services;

import com.lowagie.text.DocumentException;
import com.tmp.authentication.authorization.jwt.entities.Certificate;
import com.tmp.authentication.authorization.jwt.entities.EmployeesCertificate;
import com.tmp.authentication.authorization.jwt.entities.EmployeesCertificateId;
import com.tmp.authentication.authorization.jwt.entities.User;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final EmployeesCertificateRepository employeesCertificateRepository;

    private final UserService userService;

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

    private String generateCertificateURL(String certificateName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(apiPath + "/")
                .path(certificateName)
                .toUriString();
    }

    /*
        Methods from CertificateController
     */
    public CertificateDTO createCertificateDTO(CreateCertificateDTO createCertificateDTO, String template) {
        User user = userService.findUserById(createCertificateDTO.getEmployeeId());

        String certificateName = "Certificate" +
                "-" + createCertificateDTO.getCourseName().replace(" ", "") +
                "_" + createCertificateDTO.getEmployeeId().toString();

        String certificatePath = generateCertificateURL(certificateName);

        Certificate certificate = Certificate.builder()
                .name(certificateName)
                .path(certificatePath)
                .build();

        Certificate savedCertificate = certificateRepository.save(certificate);
        certificateName = certificateName + "_" + savedCertificate.getId();
        certificatePath = generateCertificateURL(certificateName);
        savedCertificate.setName(certificateName);
        savedCertificate.setPath(certificatePath);
        Certificate updateCertificate = certificateRepository.save(savedCertificate);

        EmployeesCertificateId employeesCertificateId = EmployeesCertificateId.builder()
                .idCertificate(updateCertificate.getId())
                .idEmployee(user.getId())
                .build();

        EmployeesCertificate employeesCertificate = EmployeesCertificate.builder()
                .id(employeesCertificateId)
                .idCertificate(updateCertificate)
                .idEmployee(user)
                .build();

        employeesCertificateRepository.save(employeesCertificate);

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeFirstName", user.getFirstName());
        variables.put("employeeLastName", user.getLastName());
        variables.put("employeeNumber", user.getEmployeeNumber());
        variables.put("courseName", createCertificateDTO.getCourseName());
        variables.put("courseCategory", createCertificateDTO.getCourseCategory());
        variables.put("courseCompletedDate", updateCertificate.getReleaseDate());

        Context context = new Context();
        context.setVariables(variables);

        String html = springTemplateEngine.process(template, context);

        String pdfCertificateName = updateCertificate.getName() + ".pdf";

        // Convert HTML to PDF and save in FileSystem
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(certificatesPath + "/" + pdfCertificateName);
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
