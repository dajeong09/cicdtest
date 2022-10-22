package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.request.CourseRequestDto;
import com.innocamp.dduha.dto.request.TripRequestDto;
import com.innocamp.dduha.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.xml.bind.ValidationException;


@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/auth/trip")
    public ResponseEntity<?> createTrip(@RequestBody TripRequestDto requestDto) {
        return tripService.createTrip(requestDto);
    }

    @GetMapping("/auth/trip")
    public ResponseEntity<?> getMyTrips() {
      return tripService.getMyTrips();
    }

    @GetMapping("/auth/trip/{id}")
    public ResponseEntity<?> getMyTripInfo(@PathVariable Long id) throws AuthenticationException {
        return tripService.getMyTripInfo(id);
    }

    @PutMapping("/auth/trip/{id}")
    public ResponseEntity<?> modifyMyTrip(@PathVariable Long id, @RequestBody TripRequestDto requestDto) throws AuthenticationException {
        return tripService.modifyMyTrip(id, requestDto);
    }

    @DeleteMapping("/auth/trip/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long id) throws AuthenticationException {
        return tripService.deleteTrip(id);
    }

    @GetMapping("/trip")
    public ResponseEntity<?> getPublicTrips(@RequestParam(value = "page") int page) {
        return tripService.getPublicTrips(page);
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<?> getPublicTripInfo(@PathVariable Long id) {
        return tripService.getPublicTripInfo(id);
    }

    @PostMapping("/auth/trip/course")
    public ResponseEntity<?> saveCourseDetailOrder(@RequestBody CourseRequestDto courseRequestDto) throws ValidationException, AuthenticationException {
        return tripService.saveCourseDetailOrder(courseRequestDto);
    }
}
