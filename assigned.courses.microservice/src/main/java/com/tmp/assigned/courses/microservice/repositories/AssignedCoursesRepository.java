package com.tmp.assigned.courses.microservice.repositories;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignedCoursesRepository  extends JpaRepository<AssignedCourses, Long> {

    Boolean existsAssignedCoursesByIdCourseAndIdEmployee(Long idCourse, Long idEmployee);

    List<AssignedCourses> getAssignedCoursesByIdEmployeeAndCompleted(Long idEmployee, Boolean completed);

    Optional<AssignedCourses> findAssignedCoursesByIdCourseAndIdEmployee(Long idCourse, Long idEmployee);

    Integer countAssignedCoursesByIdEmployeeAndCompleted(Long idEmployee, Boolean completed);
}
