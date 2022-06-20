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

import java.net.URI;

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
            URI uri = URI.create(
                    String.format("http://auth:8090/api/1.0/tmp/auth/getUser/%d",
                            createAssignCourseDTO.getIdEmployee()));

            SuccessResponseEmployee employee = restTemplate.getForObject(uri, SuccessResponseEmployee.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error(httpClientErrorException.getMessage());
        }

        return null;
    }
}
