package com.example.demo.repository;

import com.example.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    boolean existsByNameAndCourseCategoryId(String name, Long courseCategoryId);
    boolean existsByNameAndCourseCategoryIdAndIdNot(String name, Long courseCategoryId, Long id);
    List<Course> findByCourseCategoryId(Long categoryId);
}