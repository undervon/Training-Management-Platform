package com.tmp.courses.microservice.controllers;

import com.tmp.courses.microservice.models.AddCourseDTO;
import com.tmp.courses.microservice.models.SuccessResponseDTO;
import com.tmp.courses.microservice.services.CourseService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class CourseController {

    private final CourseService courseService;

    @CrossOrigin
    @PostMapping(value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> addCourseReq(
            @RequestPart("addCourse") @Parameter(schema = @Schema(type = "string", format = "binary")) AddCourseDTO addCourseDTO,
            @RequestPart("file") MultipartFile[] files) {
        log.info("[ {} ] -> [ {} ] -> [ addCourseReq ] addCourseDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, addCourseDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.builder()
                        .data(courseService.addCourseReq(addCourseDTO, files))
                        .build());
    }

    @CrossOrigin
    @GetMapping(value = "/getCourses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getCoursesByCategoryParamReq(
            @RequestParam(name = "category") String category) {
        log.info("[ {} ] -> [ {} ] -> [ getCoursesByCategoryParamReq ] category: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, category);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(courseService.getCoursesByCategoryParamReq(category))
                        .build());
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteCourse/{id}")
    public ResponseEntity<?> deleteCourseByIdReq(@PathVariable(value = "id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ deleteCourseById ] id: {}",
                this.getClass().getSimpleName(), HttpMethod.DELETE, id);

        courseService.deleteCourseByIdReq(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @CrossOrigin
    @GetMapping(value = "/getCourse/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getCourseByIdReq(@PathVariable(value = "id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ getCourseByIdReq ] id: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(courseService.getCourseByIdReq(id))
                        .build());
    }

    @CrossOrigin
    @GetMapping(value = "/{directory}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getFilesFromDirectoryReq(
            @PathVariable(value = "directory") String directory) {
        log.info("[ {} ] -> [ {} ] -> [ getFilesFromDirectoryReq ] directory: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, directory);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(courseService.getFilesFromDirectoryReq(directory))
                        .build());
    }

    @CrossOrigin
    @GetMapping(value = "/{directory}/{filename}")
    public ResponseEntity<Resource> getFileReq(@PathVariable(value = "directory") String directory,
            @PathVariable(value = "filename") String filename) {
        log.info("[ {} ] -> [ {} ] -> [ getFileReq ] directory: {}, filename: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, directory, filename);

        Resource file = courseService.getFileReq(directory, filename);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);

    }
}
