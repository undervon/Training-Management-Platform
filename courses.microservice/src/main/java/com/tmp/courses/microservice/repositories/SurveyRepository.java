package com.tmp.courses.microservice.repositories;

import com.tmp.courses.microservice.entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
