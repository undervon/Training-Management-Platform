package com.tmp.assigned.courses.microservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assigned_courses")
public class AssignedCourses {

    @Id
    @Column(name = "id_assigned_course")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    private String state;

    @Builder.Default
    private Boolean completed = false;

    private Long idEmployee;
}
