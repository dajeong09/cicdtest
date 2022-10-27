package com.innocamp.dduha.domain.trip.controller;

import com.innocamp.dduha.domain.trip.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.domain.trip.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/auth/course/details")
    public ResponseEntity<?> addCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto) {
        return courseService.addCourseDetail(courseDetailRequestDto);
    }

    @DeleteMapping("/auth/course/details")
    public ResponseEntity<?> removeCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto) {
        return courseService.removeCourseDetail(courseDetailRequestDto);
    }

    @GetMapping("/course/bookmark")
    public ResponseEntity<?> getBookmarkList() {
        return courseService.getBookmarkList();
    }

    @GetMapping("/course/nearby")
    public ResponseEntity<?> getNearbyList(@RequestParam(value="latitude") double latitude
            , @RequestParam(value="longitude") double longitude) {
        return courseService.getNearbyList(latitude, longitude);
    }
}
