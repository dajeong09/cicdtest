package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.CourseRequestDto;
import com.innocamp.dduha.dto.request.TripRequestDto;
import com.innocamp.dduha.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/auth/trip")
    public ResponseDto<?> createTrip(@RequestBody TripRequestDto requestDto, HttpServletRequest request) {
        return tripService.createTrip(requestDto, request);
    }

    @GetMapping("/auth/trip")
    public ResponseDto<?> getMyTrips(HttpServletRequest request) {  //사용자 검증 후 변경
      return tripService.getMyTrips(request);
    }

    @GetMapping("/auth/trip/{id}")
    public ResponseDto<?> getTripInfo(@PathVariable Long id,  HttpServletRequest request) {
        return tripService.getMyTripInfo(id, request);
    }

    @DeleteMapping("/auth/trip/{id}")
    public ResponseDto<?> deleteTrip(@PathVariable Long id, HttpServletRequest request) {
        return tripService.deleteTrip(id, request);
    }

    @GetMapping("/trip")
    public ResponseDto<?> getPublicTrips() {
        return tripService.getPublicTrips();
    }

    @PostMapping("/auth/trip/course")
    public ResponseDto<?> createCourse(@RequestBody CourseRequestDto courseRequestDto, HttpServletRequest request) {
        return tripService.createCourse(courseRequestDto, request);
    }
}
