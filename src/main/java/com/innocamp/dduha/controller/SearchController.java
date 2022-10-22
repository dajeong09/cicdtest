package com.innocamp.dduha.controller;

import com.innocamp.dduha.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // 관광지 검색
    @GetMapping("/touristspot/search")
    public ResponseEntity<?> searchTouristSpot(@RequestParam(value="region", required = false) String region,
                                               @RequestParam(value="keyword") String keyword) {
        return searchService.searchTouristSpot(region, keyword);
    }

    // 맛집 검색
    @GetMapping("/restaurant/search")
    public ResponseEntity<?> searchRestaurant(@RequestParam(value="region", required = false) String region,
                                           @RequestParam(value="keyword") String keyword) {
        return searchService.searchRestaurant(region, keyword);

    }

    //숙소 검색
    @GetMapping("/accommodation/search")
    public ResponseEntity<?> searchAccommodation(@RequestParam(value="region", required = false) String region,
                                              @RequestParam(value="keyword") String keyword) {
        return searchService.searchAccommodation(region, keyword);

    }
}
