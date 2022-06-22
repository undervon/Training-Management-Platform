package com.tmp.courses.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseCompactedCourse {

    private String status;
    private List<CompactedCourse> data;
}
