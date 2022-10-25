package com.innocamp.dduha.domain.bookmark.controller;

import com.innocamp.dduha.domain.bookmark.service.AccommodationBookmarkService;
import com.innocamp.dduha.domain.bookmark.service.RestaurantBookmarkService;
import com.innocamp.dduha.domain.bookmark.service.TouristSpotBookmarkService;
import com.innocamp.dduha.domain.bookmark.service.TripBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final TouristSpotBookmarkService touristSpotBookmarkService;
    private final RestaurantBookmarkService restaurantBookmarkService;
    private final AccommodationBookmarkService accommodationBookmarkService;
    private final TripBookmarkService tripBookmarkService;

    // 관광지 즐겨찾기 / 취소하기
    @GetMapping("/auth/touristspot/bookmark/{id}")
    public ResponseEntity<?> createTouristSpotBookmark(@PathVariable Long id) {
        return touristSpotBookmarkService.createTouristSpotBookmark(id);
    }

    // 맛집 즐겨찾기 / 취소하기
    @GetMapping("/auth/restaurant/bookmark/{id}")
    public ResponseEntity<?> createRestaurantBookmark(@PathVariable Long id) {
        return restaurantBookmarkService.createRestaurantBookmark(id);
    }

    // 숙소 즐겨찾기 / 취소하기
    @GetMapping("/auth/accommodation/bookmark/{id}")
    public ResponseEntity<?> createAccommodationBookmark(@PathVariable Long id) {
        return accommodationBookmarkService.createAccommodationBookmark(id);
    }

    // 일정 즐겨찾기 / 취소하기
    @GetMapping("/auth/trip/bookmark/{id}")
    public ResponseEntity<?> createTripBookmark(@PathVariable Long id) {
        return tripBookmarkService.createTripBookmark(id);
    }





}
