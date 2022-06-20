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
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
public class AssignedCoursesService {

    private final AssignedCoursesRepository assignedCoursesRepository;

    private final RestTemplate restTemplate;

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
                            "http://localhost:8090/api/1.0/tmp/auth/getUser/" + createAssignCourseDTO.getIdEmployee(),
                            SuccessResponseEmployee.class);

            SuccessResponseCourse course =
                    restTemplate.getForObject(
                            "http://localhost:8092/api/1.0/tmp/courses/getCourse/"
                                    + createAssignCourseDTO.getIdCourse(),
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
