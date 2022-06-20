package com.tmp.assigned.courses.microservice.repositories;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignedCoursesRepository  extends JpaRepository<AssignedCourses, Long> {

    Boolean existsAssignedCoursesByIdCourseAndIdEmployee(Long idCourse, Long idEmployee);
}
