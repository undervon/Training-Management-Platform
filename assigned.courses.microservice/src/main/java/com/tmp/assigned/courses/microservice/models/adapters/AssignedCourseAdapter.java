package com.tmp.assigned.courses.microservice.models.adapters;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AssignedCourseAdapter {

    public static AssignCourseDTO assignedCourseToAssignCourseDTO(AssignedCourses assignedCourses) {
        return AssignCourseDTO.builder()
                .id(assignedCourses.getId())
                .date(assignedCourses.getDate())
                .completed(assignedCourses.getCompleted())
                .idEmployee(assignedCourses.getIdEmployee())
                .idCourse(assignedCourses.getIdCourse())
                .build();
    }
}
