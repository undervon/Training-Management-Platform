package com.tmp.courses.microservice.services;

import com.tmp.courses.microservice.entities.Course;
import com.tmp.courses.microservice.models.AddCourseDTO;
import com.tmp.courses.microservice.models.CourseDTO;
import com.tmp.courses.microservice.models.CoursesCategoryDTO;
import com.tmp.courses.microservice.models.EditCourseDTO;
import com.tmp.courses.microservice.models.FileInfoDTO;
import com.tmp.courses.microservice.models.GetCourseDTO;
import com.tmp.courses.microservice.repositories.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private AddCourseDTO addCourseDTO;
    private Course course;
    private CourseDTO courseDTO;
    private CoursesCategoryDTO coursesCategoryDTO;
    private GetCourseDTO getCourseDTO;
    private FileInfoDTO fileInfoDTO;
    private EditCourseDTO editCourseDTO;

    @BeforeEach
    void setUp() {
        addCourseDTO = AddCourseDTO.builder()
                .name("A")
                .description("A")
                .language("English")
                .requirements("A")
                .category("Programming")
                .duration("A")
                .timeToMake("A")
                .containsCertificate(true)
                .build();

        course = Course.builder()
                .id(1L)
                .name("A")
                .description("A")
                .language("English")
                .requirements("A")
                .category("Programming")
                .rating(0.0)
                .ratedNumber(0)
                .date(LocalDateTime.now())
                .duration("A")
                .timeToMake("A")
                .containsCertificate(true)
                .path("A")
                .build();

        courseDTO = CourseDTO.builder()
                .id(1L)
                .name("A")
                .description("A")
                .language("English")
                .requirements("A")
                .category("Programming")
                .rating(0.0)
                .ratedNumber(0)
                .date(LocalDateTime.now())
                .duration("A")
                .timeToMake("A")
                .containsCertificate(true)
                .path("A")
                .build();

        coursesCategoryDTO = CoursesCategoryDTO.builder()
                .id(1L)
                .name("A")
                .description("A")
                .timeToMake("A")
                .rating(0.0)
                .build();

        fileInfoDTO = FileInfoDTO.builder()
                .filePath("A")
                .filename("A")
                .build();

        getCourseDTO = GetCourseDTO.builder()
                .id(1L)
                .name("A")
                .description("A")
                .language("English")
                .requirements("A")
                .category("Programming")
                .rating(0.0)
                .ratedNumber(0)
                .date(LocalDateTime.now())
                .duration("A")
                .timeToMake("A")
                .containsCertificate(true)
                .path("A")
                .fileInfo(List.of())
                .build();

        editCourseDTO = EditCourseDTO.builder()
                .name("A")
                .description("A")
                .language("English")
                .requirements("A")
                .category("Programming")
                .duration("A")
                .timeToMake("A")
                .containsCertificate(true)
                .build();
    }

    @Test
    void addCourseReq() {
        MultipartFile[] multipartFiles = new MultipartFile[1];
        multipartFiles[0] = new MockMultipartFile("A", new byte[1]);

        Mockito.when(courseRepository.save(course)).thenReturn(course);

        Assertions.assertEquals(courseDTO, courseService.addCourseReq(addCourseDTO, multipartFiles));
    }

    @Test
    void getCoursesByCategoryParamReq() {
        Mockito.when(courseRepository.getCoursesByCategory("A")).thenReturn(List.of(course));

        Assertions.assertEquals(List.of(coursesCategoryDTO),
                courseService.getCoursesByCategoryParamReq("A", course.getId()));
    }

    @Test
    void deleteCourseByIdReq() {
        Mockito.when(courseRepository.findCourseById(course.getId())).thenReturn(Optional.of(course));

        courseRepository.delete(course);

        courseService.deleteCourseByIdReq(course.getId());
    }

    @Test
    void getCourseByIdReq() {
        Mockito.when(courseRepository.findCourseById(course.getId())).thenReturn(Optional.of(course));

        Assertions.assertEquals(getCourseDTO, courseService.getCourseByIdReq(course.getId()));
    }

    @Test
    void getFilesFromDirectoryReq() {
        Mockito.when(courseRepository.findCourseByPath(course.getPath())).thenReturn(Optional.of(course));

        Assertions.assertEquals(List.of(fileInfoDTO), courseService.getFilesFromDirectoryReq("A"));
    }

    @Test
    void getFileReq() throws IOException {
        Mockito.when(courseRepository.findCourseByPath(course.getPath())).thenReturn(Optional.of(course));

        Assertions.assertEquals(1, courseService.getFileReq("A", "A").contentLength());
    }

    @Test
    void updateCourseByIdReq() {
        Mockito.when(courseRepository.findCourseById(course.getId())).thenReturn(Optional.of(course));

        courseRepository.save(course);

        courseService.updateCourseByIdReq(course.getId(), editCourseDTO);
    }
}
