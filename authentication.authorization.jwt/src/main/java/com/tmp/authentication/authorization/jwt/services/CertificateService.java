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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Resource load(File certificatePath, String filename) {
        try {
            Path file = Paths.get(certificatePath.toString())
                    .resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(String.format("Could not read file %s", filename));
            }
        } catch (MalformedURLException malformedURLException) {
            throw new StorageException(String.format("Could not read file %s (%s)", filename,
                    malformedURLException.getMessage()));
        }
    }

    private void delete(File certificatePath) {
        FileSystemUtils.deleteRecursively(Paths.get(certificatePath.toString())
                .toFile());
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
        Path route = Paths.get(certificatesPath);

        if (!Files.exists(route)) {
            init();
        }

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String courseCompletedDateFormat = formatter.format(updateCertificate.getReleaseDate());

        // Create a Map with all variables and their values from template .html file
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeFirstName", user.getFirstName());
        variables.put("employeeLastName", user.getLastName());
        variables.put("employeeNumber", user.getEmployeeNumber());
        variables.put("courseName", createCertificateDTO.getCourseName());
        variables.put("courseCategory", createCertificateDTO.getCourseCategory());
        variables.put("courseCompletedDate", courseCompletedDateFormat);

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

    @Transactional
    public List<CertificateDTO> getCertificatesByUserIdReq(Long id) {
        User user = userService.findUserById(id);

        List<EmployeesCertificate> employeesCertificateList =
                employeesCertificateRepository.getEmployeesCertificatesByIdEmployee(user);

        List<Certificate> certificateList = employeesCertificateList.stream()
                .map(employeesCertificate -> certificateRepository.getReferenceById(
                        employeesCertificate.getIdCertificate().getId()))
                .collect(Collectors.toList());

        List<Certificate> newCertificateList = new ArrayList<>();
        for (Certificate certificate : certificateList) {
            File certificatePath = new File(certificatesPath, certificate.getName() + ".pdf");
            LocalDateTime newDate = LocalDateTime.now().minusMonths(certificate.getAvailability());
            if (certificate.getReleaseDate().isBefore(newDate)) {
                delete(certificatePath);

                Certificate newCertificate = Certificate.builder()
                        .id(certificate.getId())
                        .name(certificate.getName())
                        .releaseDate(certificate.getReleaseDate())
                        .availability(certificate.getAvailability())
                        .path(certificate.getPath())
                        .users(certificate.getUsers())
                        .build();
                newCertificateList.add(newCertificate);

                certificateRepository.deleteCertificateById(certificate.getId());
                employeesCertificateRepository.deleteEmployeesCertificateByIdCertificateAndIdEmployee(certificate,
                        user);
            }
        }

        certificateList.removeAll(newCertificateList);

        return CertificateAdapter.certificateListToCertificateDTOList(certificateList);
    }

    public Resource getPdfReq(String pdfName) {
        File pdfPath = new File(certificatesPath);

        return load(pdfPath, pdfName + ".pdf");
    }
}
