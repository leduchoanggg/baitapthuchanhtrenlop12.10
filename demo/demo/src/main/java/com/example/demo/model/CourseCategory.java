package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "course_categories")
@Data
public class CourseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên loại khóa học không được để trống")
    private String name;

    private String description;

    @OneToMany(mappedBy = "courseCategory", cascade = CascadeType.ALL)
    private List<Course> courses;
}