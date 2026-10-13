package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseCategoryRepository;
import com.example.demo.repository.CourseImageRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseCategoryService;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCategoryRepository categoryRepository;

    @Autowired
    private CourseImageRepository imageRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseCategoryService categoryService;

    // 1. Trang danh sách, tìm kiếm, lọc và sắp xếp (Chức năng 2 & 3)
    @GetMapping
    public String listCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Double minFee,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(required = false) String sortField,
            Model model) {

        // Xây dựng câu truy vấn động theo điều kiện tìm kiếm và lọc
Specification<Course> spec = (root, query, cb) -> cb.conjunction();
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("courseCategoryId"), categoryId));
        }
        if (isActive != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isActive"), isActive));
        }
        if (minFee != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("tuitionFee"), minFee));
        }
        if (maxFee != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("tuitionFee"), maxFee));
        }

        // Xử lý sắp xếp (Học phí tăng/giảm, Thời lượng tăng/giảm)
        Sort sort = Sort.unsorted();
        if ("fee_asc".equals(sortField)) {
            sort = Sort.by("tuitionFee").ascending();
        } else if ("fee_desc".equals(sortField)) {
            sort = Sort.by("tuitionFee").descending();
        } else if ("duration_asc".equals(sortField)) {
            sort = Sort.by("duration").ascending();
        }

        List<Course> courses = courseRepository.findAll(spec, sort);

        model.addAttribute("courses", courses);
        model.addAttribute("categories", categoryRepository.findAll());
        
        // Giữ lại các giá trị đã nhập trên form (Yêu cầu đề bài)
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("isActive", isActive);
        model.addAttribute("minFee", minFee);
        model.addAttribute("maxFee", maxFee);
        model.addAttribute("sortField", sortField);

        return "course/list";
    }

    // 2. Trang chi tiết khóa học và quản lý ảnh (Chức năng 4)
    @GetMapping("/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("images", imageRepository.findByCourseId(id));
        return "course/detail";
    }

    // 3. Đổi ảnh đại diện (Thumbnail)
    @PostMapping("/{courseId}/set-thumbnail/{imageId}")
    public String setThumbnail(@PathVariable Long courseId, @PathVariable Long imageId) {
        courseService.setThumbnail(courseId, imageId);
        return "redirect:/courses/" + courseId;
    }

    // 4. Xóa loại khóa học (Có kiểm tra ràng buộc 1)
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/courses";
    }
}