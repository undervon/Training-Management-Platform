package com.tmp.courses.microservice.repositories;

import com.tmp.courses.microservice.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> getCoursesByCategory(String category);

    Optional<Course> findCourseById(Long id);

    Optional<Course> findCourseByPath(String path);
}
