package com.tmp.assigned.courses.microservice.services;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import com.tmp.assigned.courses.microservice.exceptions.CourseAlreadyAssignedException;
import com.tmp.assigned.courses.microservice.exceptions.GenericException;
import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.CoursesStatisticsDTO;
import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.adapters.AssignedCourseAdapter;
import com.tmp.assigned.courses.microservice.repositories.AssignedCoursesRepository;
import com.tmp.assigned.courses.microservice.vo.CompactedCourse;
import com.tmp.assigned.courses.microservice.vo.Course;
import com.tmp.assigned.courses.microservice.vo.SuccessResponseCourse;
import com.tmp.assigned.courses.microservice.vo.SuccessResponseEmployee;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AssignedCoursesService {

    private final AssignedCoursesRepository assignedCoursesRepository;

    private final RestTemplate restTemplate;

    @Value("${auth.path}")
    private String authPath;

    @Value("${auth.port}")
    private String authPort;

    @Value("${courses.path}")
    private String coursesPath;

    @Value("${courses.port}")
    private String coursesPort;

    /*
        AssignedCoursesService methods
     */
    protected List<AssignedCourses> getAssignedCoursesCompleted(Long idEmployee) {
        return assignedCoursesRepository.getAssignedCoursesByIdEmployeeAndCompleted(idEmployee, true);
    }

    protected List<AssignedCourses> getAssignedCoursesIncomplete(Long idEmployee) {
        return assignedCoursesRepository.getAssignedCoursesByIdEmployeeAndCompleted(idEmployee, false);
    }

    protected SuccessResponseEmployee getEmployeeById(Long idEmployee) {
        return restTemplate.getForObject(String.format("http://%s:%s/api/1.0/tmp/auth/getUser/%s",
                        authPath,
                        authPort,
                        idEmployee.toString()),
                SuccessResponseEmployee.class);
    }

    protected SuccessResponseCourse getCourseById(Long idCourse) {
        return restTemplate.getForObject(String.format("http://%s:%s/api/1.0/tmp/courses/getCourse/%s",
                        coursesPath,
                        coursesPort,
                        idCourse.toString()),
                SuccessResponseCourse.class);
    }

    protected Integer countAssignedCoursesCompleted(Long idEmployee) {
        return assignedCoursesRepository.countAssignedCoursesByIdEmployeeAndCompleted(idEmployee, true);
    }

    protected Integer countAssignedCoursesIncomplete(Long idEmployee) {
        return assignedCoursesRepository.countAssignedCoursesByIdEmployeeAndCompleted(idEmployee, false);
    }

    /*
        Methods from AssignedCoursesController
     */
    public AssignCourseDTO assignUserCourseReq(CreateAssignCourseDTO createAssignCourseDTO) {
        try {
            SuccessResponseEmployee employee = getEmployeeById(createAssignCourseDTO.getIdEmployee());

            SuccessResponseCourse course = getCourseById(createAssignCourseDTO.getIdCourse());

            Long idEmployee = employee.getData().getId();
            Long idCourse = course.getData().getId();

            if (assignedCoursesRepository.existsAssignedCoursesByIdCourseAndIdEmployee(idCourse, idEmployee)) {
                throw new CourseAlreadyAssignedException(idCourse.toString());
            }

            AssignedCourses assignedCourses = AssignedCourses.builder()
                    .idEmployee(idEmployee)
                    .idCourse(idCourse)
                    .build();

            return AssignedCourseAdapter.assignedCourseToAssignCourseDTO(
                    assignedCoursesRepository.save(assignedCourses));
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
    }

    public void setCompletedCourseReq(CreateAssignCourseDTO createAssignCourseDTO) {
        try {
            SuccessResponseCourse course = getCourseById(createAssignCourseDTO.getIdCourse());
            SuccessResponseEmployee employee = getEmployeeById(createAssignCourseDTO.getIdEmployee());

            AssignedCourses assignedCourses = assignedCoursesRepository.getAssignedCoursesByIdCourseAndIdEmployee(
                    course.getData().getId(),
                    employee.getData().getId());

            assignedCourses.setCompleted(true);

            assignedCoursesRepository.save(assignedCourses);
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
    }

    public List<CompactedCourse> getCompletedCoursesReq(Long idEmployee) {
        try {
            // Check if the employee exists
            getEmployeeById(idEmployee);

            List<AssignedCourses> assignedCoursesList = getAssignedCoursesCompleted(idEmployee);

            List<Course> courseList = assignedCoursesList.stream()
                    .map(assignedCourses -> getCourseById(assignedCourses.getIdCourse()).getData())
                    .collect(Collectors.toList());

            return courseList.stream()
                    .map(course -> CompactedCourse.builder()
                            .id(course.getId())
                            .name(course.getName())
                            .description(course.getDescription())
                            .build())
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
    }

    public List<CompactedCourse> getIncompleteCoursesReq(Long idEmployee) {
        try {
            // Check if the employee exists
            getEmployeeById(idEmployee);

            List<AssignedCourses> assignedCoursesList = getAssignedCoursesIncomplete(idEmployee);

            List<Course> courseList = assignedCoursesList.stream()
                    .map(assignedCourses -> getCourseById(assignedCourses.getIdCourse()).getData())
                    .collect(Collectors.toList());

            return courseList.stream()
                    .map(course -> CompactedCourse.builder()
                            .id(course.getId())
                            .name(course.getName())
                            .description(course.getDescription())
                            .build())
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
    }

    public CoursesStatisticsDTO getCoursesStatisticsReq(Long idEmployee) {
        try {
            // Check if the employee exists
            SuccessResponseEmployee employee = getEmployeeById(idEmployee);

            return CoursesStatisticsDTO.builder()
                    .countAssignedCourses(countAssignedCoursesIncomplete(employee.getData().getId()))
                    .countCompletedCourses(countAssignedCoursesCompleted(employee.getData().getId()))
                    .build();
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
    }
}