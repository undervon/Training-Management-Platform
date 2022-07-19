package com.tmp.assigned.courses.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompactedCourse {

    private Long id;

    private String name;
    private String description;

    private Double rating;
}
