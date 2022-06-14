package com.tmp.courses.microservice.services;

import com.tmp.courses.microservice.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
}
