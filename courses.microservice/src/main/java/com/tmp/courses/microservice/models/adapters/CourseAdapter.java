package com.tmp.courses.microservice.models.adapters;

import com.tmp.courses.microservice.entities.Course;
import com.tmp.courses.microservice.models.CourseDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CourseAdapter {

    public static CourseDTO courseToCourseDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .language(course.getLanguage())
                .requirements(course.getRequirements())
                .category(course.getCategory())
                .rating(course.getRating())
                .ratedNumber(course.getRatedNumber())
                .date(course.getDate())
                .duration(course.getDuration())
                .timeToMake(course.getTimeToMake())
                .path(course.getPath())
                .containsCertificate(course.getContainsCertificate())
                .build();
    }
}
