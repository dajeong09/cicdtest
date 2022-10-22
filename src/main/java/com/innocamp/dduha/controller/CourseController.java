package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.xml.bind.ValidationException;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/auth/course/details")
    public ResponseEntity<?> addCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto)
            throws ValidationException, AuthenticationException {
        return courseService.addCourseDetail(courseDetailRequestDto);
    }

    @DeleteMapping("/auth/course/details")
    public ResponseEntity<?> removeCourseDetail(@RequestBody CourseDetailRequestDto courseDetailRequestDto)
            throws ValidationException, AuthenticationException {
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
