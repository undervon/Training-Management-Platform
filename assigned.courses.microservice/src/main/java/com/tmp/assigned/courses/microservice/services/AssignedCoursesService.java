package com.tmp.assigned.courses.microservice.services;

import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.repositories.AssignedCoursesRepository;
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
    public AssignCourseDTO assignUserCourseReq(CreateAssignCourseDTO createAssignCourseDTO) {
        try {
            SuccessResponseEmployee employee =
                    restTemplate.getForObject(
                            "http://AUTHENTICATION-AUTHORIZATION-JWT/api/1.0/tmp/auth/getUser/"
                                    + createAssignCourseDTO.getIdEmployee(),
                            SuccessResponseEmployee.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error(httpClientErrorException.getMessage());
        }

        return null;
    }
}
