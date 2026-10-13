package com.example.demo.repository;

import com.example.demo.model.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseImageRepository extends JpaRepository<CourseImage, Long> {
    List<CourseImage> findByCourseId(Long courseId);
}