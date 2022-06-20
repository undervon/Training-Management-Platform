package com.tmp.assigned.courses.microservice.services;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import com.tmp.assigned.courses.microservice.exceptions.CourseAlreadyAssignedException;
import com.tmp.assigned.courses.microservice.exceptions.GenericException;
import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.adapters.AssignedCourseAdapter;
import com.tmp.assigned.courses.microservice.repositories.AssignedCoursesRepository;
import com.tmp.assigned.courses.microservice.vo.SuccessResponseCourse;
import com.tmp.assigned.courses.microservice.vo.SuccessResponseEmployee;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    /*
        Methods from AssignedCoursesController
     */
//    auth in loc de localhost
//    courses in loc de localhost
    public AssignCourseDTO assignUserCourseReq(CreateAssignCourseDTO createAssignCourseDTO) {
        try {
            SuccessResponseEmployee employee =
                    restTemplate.getForObject(
                            String.format("http://%s:%s/api/1.0/tmp/auth/getUser/%s",
                                    authPath,
                                    authPort,
                                    createAssignCourseDTO.getIdEmployee().toString()),
                            SuccessResponseEmployee.class);

            SuccessResponseCourse course =
                    restTemplate.getForObject(
                            String.format("http://%s:%s/api/1.0/tmp/courses/getCourse/%s",
                                    coursesPath,
                                    coursesPort,
                                    createAssignCourseDTO.getIdCourse().toString()),
                            SuccessResponseCourse.class);

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
}
