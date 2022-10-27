package com.innocamp.dduha.domain.place.accommodation.controller;

import com.innocamp.dduha.domain.place.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/accommodation")
    public ResponseEntity<?> getAccommodationList(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "station", required = false) String station) {
        return accommodationService.getAccommodationList(page, region, station);
    }

    @GetMapping("/accommodation/{id}")
    public ResponseEntity<?> getAccommodationDetail(@PathVariable Long id) {
        return accommodationService.getAccommodationDetail(id);
    }
}
