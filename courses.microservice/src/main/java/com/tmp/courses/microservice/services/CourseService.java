package com.tmp.courses.microservice.services;

import com.tmp.courses.microservice.entities.Course;
import com.tmp.courses.microservice.exceptions.CourseNotFoundException;
import com.tmp.courses.microservice.exceptions.StorageException;
import com.tmp.courses.microservice.models.AddCourseDTO;
import com.tmp.courses.microservice.models.CourseDTO;
import com.tmp.courses.microservice.models.CoursesCategoryDTO;
import com.tmp.courses.microservice.models.EditCourseDTO;
import com.tmp.courses.microservice.models.FileInfoDTO;
import com.tmp.courses.microservice.models.GetCourseDTO;
import com.tmp.courses.microservice.models.adapters.CourseAdapter;
import com.tmp.courses.microservice.repositories.CourseRepository;
import com.tmp.courses.microservice.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final SurveyRepository surveyRepository;

    @Value("${courses.path}")
    private String coursesPath;

    @Value("${api.path}")
    private String apiPath;

    /*
        CourseService methods
     */
    private void init(String uploadPath) {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException ioException) {
            throw new StorageException(String.format("Could not initialize storage (%s).", ioException.getMessage()));
        }
    }

    private void store(MultipartFile[] files, Long id, String name) {
        // Parse to right path
        Path route = Paths.get(coursesPath);

        if (!Files.exists(route)) {
            init(coursesPath);
        }

        // Create new path with id and course name (put / between coursesPath and id.toString() + "-" + name)
        File coursePath = new File(coursesPath, id.toString() + "-" + name.replace(" ", ""));

        // Parse to right path
        Path newRoute = Paths.get(coursePath.toString());

        init(coursePath.toString());

        Arrays.asList(files).forEach(file -> {
            try {
                Files.copy(file.getInputStream(), newRoute.resolve(file.getOriginalFilename().replace(" ", "_")));
            } catch (IOException ioException) {
                throw new StorageException(String.format("Failed to store file %s (%s).", file.getOriginalFilename(),
                        ioException.getMessage()));
            }
        });
    }

    private void checkEmptyFiles(MultipartFile[] files) {
        Arrays.asList(files).forEach(file -> {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
        });
    }

    private void delete(File coursePath) {
        FileSystemUtils.deleteRecursively(Paths.get(coursePath.toString())
                .toFile());
    }

    private List<Path> loadAll(Path route) {
        try {
            if (Files.exists(route)) {
                return Files.walk(route, 1)
                        .filter(path -> !path.equals(route))
                        .collect(Collectors.toList());
            }

            return Collections.emptyList();
        } catch (IOException ioException) {
            throw new StorageException("Could not list the files");
        }
    }

    private Resource load(File coursePath, String filename) {
        try {
            Path file = Paths.get(coursePath.toString())
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

    private void renameDirectory(File coursePath, Long id, String filename) {
        Path route = Paths.get(coursePath.toString());

        String newDirectoryName = id + "-" + filename.replace(" ", "");

        try {
            // Rename a file in the same directory
            Files.move(route, route.resolveSibling(newDirectoryName));
        } catch (IOException ioException) {
            throw new StorageException("Could not change the file name");
        }
    }

    private String generateDirectoryURL(Long id, String name) {
        final String directoryName = id.toString() + "-" + name.replace(" ", "");
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(apiPath + "/")
                .path(directoryName)
                .toUriString();
    }

    private String setDirectoryURL(String directory) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(apiPath + "/")
                .path(directory)
                .toUriString();
    }

    protected Course findCourseById(Long id) {
        return courseRepository.findCourseById(id)
                .orElseThrow(() -> new CourseNotFoundException(id.toString()));
    }

    protected Course findCourseByPath(String path) {
        return courseRepository.findCourseByPath(path)
                .orElseThrow(() -> new CourseNotFoundException(path));
    }

    protected Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    private List<FileInfoDTO> getAllFilesPath(Course course) {
        File coursePath = new File(coursesPath, course.getId().toString() + "-" + course.getName().replace(" ", ""));
        Path route = Paths.get(coursePath.toString());

        // Map all files from directory and create for each path (endpoint)
        return loadAll(route).stream()
                .map(path -> {
                    String name = path.getFileName().toString();
                    String newFilePath = course.getPath() + "/" + name;

                    return FileInfoDTO.builder()
                            .filename(name)
                            .filePath(newFilePath)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /*
        Methods from CourseController
     */
    public CourseDTO addCourseReq(AddCourseDTO addCourseDTO, MultipartFile[] files) {
        // Check if the files is empty
        checkEmptyFiles(files);

        Course course = Course.builder()
                .name(addCourseDTO.getName())
                .description(addCourseDTO.getDescription())
                .language(addCourseDTO.getLanguage())
                .requirements(addCourseDTO.getRequirements())
                .category(addCourseDTO.getCategory())
                .duration(addCourseDTO.getDuration())
                .timeToMake(addCourseDTO.getTimeToMake())
                .containsCertificate(addCourseDTO.getContainsCertificate())
                .build();

        saveCourse(course);

        course.setPath(generateDirectoryURL(course.getId(), course.getName()));

        saveCourse(course);

        // Store the files
        store(files, course.getId(), course.getName());

        return CourseAdapter.courseToCourseDTO(course);
    }

    public List<CoursesCategoryDTO> getCoursesByCategoryParamReq(String category) {
        List<Course> courses = courseRepository.getCoursesByCategory(category);

        return courses.stream()
                .map(course -> CoursesCategoryDTO.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCourseByIdReq(Long id) {
        Course course = findCourseById(id);

        String surveyName = "Survey" + "-" + course.getId();

        surveyRepository.deleteSurveysByName(surveyName);

        File coursePath = new File(coursesPath, course.getId().toString() + "-" + course.getName().replace(" ", ""));
        delete(coursePath);

        courseRepository.delete(course);
    }

    public GetCourseDTO getCourseByIdReq(Long id) {
        Course course = findCourseById(id);

        List<FileInfoDTO> fileInfoDTOs = getAllFilesPath(course);

        return GetCourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .language(course.getLanguage())
                .requirements(course.getRequirements())
                .category(course.getCategory())
                .rating(course.getRating())
                .ratedNumber(course.getRatedNumber())
                .date(course.getDate())
                .duration(course.getDuration())
                .timeToMake(course.getTimeToMake())
                .path(course.getPath())
                .containsCertificate(course.getContainsCertificate())
                .fileInfo(fileInfoDTOs)
                .build();
    }

    public List<FileInfoDTO> getFilesFromDirectoryReq(String directory) {
        String findPath = setDirectoryURL(directory);

        Course course = findCourseByPath(findPath);

        return getAllFilesPath(course);
    }

    public Resource getFileReq(String directory, String filename) {
        String findPath = setDirectoryURL(directory);

        Course course = findCourseByPath(findPath);

        // Create course path storage in local storage
        File coursePath = new File(coursesPath, course.getId().toString() + "-" + course.getName().replace(" ", ""));

        return load(coursePath, filename);
    }

    public void updateCourseByIdReq(Long id, EditCourseDTO editCourseDTO) {
        Course course = findCourseById(id);

        File coursePath = new File(coursesPath, course.getId().toString() + "-" + course.getName().replace(" ", ""));
        renameDirectory(coursePath, course.getId(), editCourseDTO.getName());

        Course newCourse = Course.builder()
                .id(course.getId())
                .name(editCourseDTO.getName())
                .description(editCourseDTO.getDescription())
                .language(editCourseDTO.getLanguage())
                .requirements(editCourseDTO.getRequirements())
                .category(editCourseDTO.getCategory())
                .duration(editCourseDTO.getDuration())
                .timeToMake(editCourseDTO.getTimeToMake())
                .containsCertificate(editCourseDTO.getContainsCertificate())
                .rating(course.getRating())
                .ratedNumber(course.getRatedNumber())
                .date(course.getDate())
                .path(generateDirectoryURL(course.getId(), editCourseDTO.getName()))
                .build();

        saveCourse(newCourse);
    }
}
