package com.example.demo.service;

import com.example.demo.model.Course;
import com.example.demo.model.CourseImage;
import com.example.demo.repository.CourseImageRepository;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseImageRepository imageRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseId không tồn tại: " + id));
    }

    @Transactional
    public void setThumbnail(Long courseId, Long imageId) {
        List<CourseImage> images = imageRepository.findByCourseId(courseId);
        boolean found = false;
        for (CourseImage img : images) {
            if (img.getId().equals(imageId)) {
                img.setIsThumbnail(true);
                found = true;
            } else {
                img.setIsThumbnail(false);
            }
        }
        if (!found) {
            throw new RuntimeException("CourseImageId không tồn tại trong khóa học này!");
        }
        imageRepository.saveAll(images);
    }
}