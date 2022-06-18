package com.tmp.assigned.courses.microservice.controllers;

import com.tmp.assigned.courses.microservice.services.AssignedCoursesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class AssignedCoursesController {

    private final AssignedCoursesService assignedCoursesService;

}
