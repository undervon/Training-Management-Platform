package com.tmp.courses.microservice.entities;

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
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "id_course")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 10000)
    private String description;
    private String language;
    private String requirements;
    private String category;

    @Builder.Default
    private Double rating = 0.0;
    @Builder.Default
    private Integer ratedNumber = 0;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    private String duration;
    private String timeToMake;

    private String path;
    private Boolean containsCertificate;
}
