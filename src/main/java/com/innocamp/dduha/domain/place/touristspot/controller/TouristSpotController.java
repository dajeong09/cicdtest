package com.innocamp.dduha.domain.place.touristspot.controller;

import com.innocamp.dduha.domain.place.touristspot.service.TouristSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TouristSpotController {

    private final TouristSpotService touristSpotService;

    @GetMapping("/touristspot")
    public ResponseEntity<?> getTouristSpotList(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "station", required = false) String station) {
        return touristSpotService.getTouristSpotList(page, region, station);
    }

    @GetMapping("/touristspot/{id}")
    public ResponseEntity<?> getTouristSpotDetail(@PathVariable Long id) {
        return touristSpotService.getTouristSpotDetail(id);
    }
}
