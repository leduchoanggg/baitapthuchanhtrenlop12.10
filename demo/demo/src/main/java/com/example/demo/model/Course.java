package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;

    @NotNull(message = "Học phí không được để trống")
    @Positive(message = "Học phí phải lớn hơn 0")
    private Double tuitionFee;

    @NotNull(message = "Thời lượng không được để trống")
    @Positive(message = "Thời lượng phải lớn hơn 0")
    private Integer duration;

    private Boolean isActive = true;

    private String description;

    @NotNull(message = "Loại khóa học không được để trống")
    @Column(name = "course_category_id")
    private Long courseCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_category_id", insertable = false, updatable = false)
    private CourseCategory courseCategory;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseImage> images;

    public String getDurationType() {
        return (duration != null && duration <= 30) ? "Ngắn hạn" : "Dài hạn";
    }
}