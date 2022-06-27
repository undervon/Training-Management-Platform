package com.tmp.assigned.courses.microservice.services;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import com.tmp.assigned.courses.microservice.exceptions.CourseAlreadyAssignedException;
import com.tmp.assigned.courses.microservice.exceptions.EmployeeCourseNotFoundException;
import com.tmp.assigned.courses.microservice.exceptions.GenericException;
import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.CoursesStatisticsDTO;
import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.GetAssignCourseDTO;
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
        try {
            return restTemplate.getForObject(String.format("http://%s:%s/api/1.0/tmp/auth/getUser/%s",
                            authPath,
                            authPort,
                            idEmployee.toString()),
                    SuccessResponseEmployee.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
    }

    protected SuccessResponseCourse getCourseById(Long idCourse) {
        try {
            return restTemplate.getForObject(String.format("http://%s:%s/api/1.0/tmp/courses/getCourse/%s",
                            coursesPath,
                            coursesPort,
                            idCourse.toString()),
                    SuccessResponseCourse.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new GenericException();
        }
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
    }

    public void setCompletedCourseReq(CreateAssignCourseDTO createAssignCourseDTO) {
        SuccessResponseCourse course = getCourseById(createAssignCourseDTO.getIdCourse());
        SuccessResponseEmployee employee = getEmployeeById(createAssignCourseDTO.getIdEmployee());

        AssignedCourses assignedCourses = assignedCoursesRepository.findAssignedCoursesByIdCourseAndIdEmployee(
                        course.getData().getId(),
                        employee.getData().getId())
                .orElseThrow(() -> new EmployeeCourseNotFoundException(course.getData().getId().toString()));

        assignedCourses.setCompleted(true);

        assignedCoursesRepository.save(assignedCourses);
    }

    public List<CompactedCourse> getCompletedCoursesReq(Long idEmployee) {
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
                        .rating(course.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CompactedCourse> getIncompleteCoursesReq(Long idEmployee) {
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
                        .rating(course.getRating())
                        .build())
                .collect(Collectors.toList());
    }

    public CoursesStatisticsDTO getCoursesStatisticsReq(Long idEmployee) {
        // Check if the employee exists
        SuccessResponseEmployee employee = getEmployeeById(idEmployee);

        return CoursesStatisticsDTO.builder()
                .countAssignedCourses(countAssignedCoursesIncomplete(employee.getData().getId()))
                .countCompletedCourses(countAssignedCoursesCompleted(employee.getData().getId()))
                .build();
    }

    public GetAssignCourseDTO getAssignedCoursePropertiesReq(Long idEmployee, Long idCourse) {
        SuccessResponseEmployee employee = getEmployeeById(idEmployee);

        SuccessResponseCourse course = getCourseById(idCourse);

        Long rightIdEmployee = employee.getData().getId();
        Long rightIdCourse = course.getData().getId();

        AssignedCourses assignedCourses = assignedCoursesRepository.findAssignedCoursesByIdCourseAndIdEmployee(
                        rightIdCourse,
                        rightIdEmployee)
                .orElseThrow(() -> new EmployeeCourseNotFoundException(rightIdCourse.toString()));

        return GetAssignCourseDTO.builder()
                .id(assignedCourses.getId())
                .completed(assignedCourses.getCompleted())
                .date(assignedCourses.getDate())
                .build();
    }
}