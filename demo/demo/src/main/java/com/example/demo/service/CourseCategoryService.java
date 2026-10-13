package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.CourseCategory;
import com.example.demo.repository.CourseCategoryRepository;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCategoryService {

    @Autowired
    private CourseCategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("CourseCategoryId không tồn tại!");
        }
        List<Course> courses = courseRepository.findByCourseCategoryId(id);
        if (!courses.isEmpty()) {
            throw new RuntimeException("Không được phép xóa loại khóa học này vì vẫn còn khóa học trực thuộc!");
        }
        categoryRepository.deleteById(id);
    }
}