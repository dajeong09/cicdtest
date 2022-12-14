package com.innocamp.dduha.domain.trip.controller;

import com.innocamp.dduha.domain.trip.dto.request.TripRequestDto;
import com.innocamp.dduha.domain.trip.service.TripService;
import com.innocamp.dduha.domain.trip.dto.request.CourseRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getMyTripInfo(@PathVariable Long id) {
        return tripService.getMyTripInfo(id);
    }

    @PutMapping("/auth/trip/{id}")
    public ResponseEntity<?> modifyMyTrip(@PathVariable Long id, @RequestBody TripRequestDto requestDto) {
        return tripService.modifyMyTrip(id, requestDto);
    }

    @DeleteMapping("/auth/trip/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long id) {
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
    public ResponseEntity<?> saveCourseDetailOrder(@RequestBody CourseRequestDto courseRequestDto) {
        return tripService.saveCourseDetailOrder(courseRequestDto);
    }
}
