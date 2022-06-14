package com.tmp.courses.microservice.controllers;

import com.tmp.courses.microservice.services.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class SurveyController {

    private final SurveyService surveyService;
}
