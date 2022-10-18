package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/auth/course/details")
    public ResponseDto<?> addCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto) {
        return courseService.addCourseDetail(courseDetailRequestDto);
    }

    @DeleteMapping("/auth/course/details")
    public ResponseDto<?> removeCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto) {
        return courseService.removeCourseDetail(courseDetailRequestDto);
    }

    @GetMapping("/course/bookmark")
    public ResponseDto<?> getBookmarkList() {
        return courseService.getBookmarkList();
    }

    @GetMapping("/course/nearby")
    public ResponseDto<?> getNearbyList(@RequestParam(value="latitude") double latitude, @RequestParam(value="longitude") double longitude) {
        return courseService.getNearbyList(latitude, longitude);
    }
}
