package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/auth/course/details")
    public ResponseDto<?> addCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto, HttpServletRequest request) {
        return courseService.addCourseDetail(courseDetailRequestDto, request);
    }

    @DeleteMapping("/auth/course/details")
    public ResponseDto<?> removeCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto, HttpServletRequest request) {
        return courseService.removeCourseDetail(courseDetailRequestDto, request);
    }
}
