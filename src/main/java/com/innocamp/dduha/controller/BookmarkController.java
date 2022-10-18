package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.AccommodationBookmarkService;
import com.innocamp.dduha.service.RestaurantBookmarkService;
import com.innocamp.dduha.service.TouristSpotBookmarkService;
import com.innocamp.dduha.service.TripBookmarkService;
import lombok.RequiredArgsConstructor;
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
    public ResponseDto<?> createTouristSpotBookmark(@PathVariable Long id) {
        return touristSpotBookmarkService.createTouristSpotBookmark(id);
    }

    // 맛집 즐겨찾기 / 취소하기
    @GetMapping("/auth/restaurant/bookmark/{id}")
    public ResponseDto<?> createRestaurantBookmark(@PathVariable Long id) {
        return restaurantBookmarkService.createRestaurantBookmark(id);
    }

    // 숙소 즐겨찾기 / 취소하기
    @GetMapping("/auth/accommodation/bookmark/{id}")
    public ResponseDto<?> createAccommodationBookmark(@PathVariable Long id) {
        return accommodationBookmarkService.createAccommodationBookmark(id);
    }

    // 일정 즐겨찾기 / 취소하기
    @GetMapping("/auth/trip/bookmark/{id}")
    public ResponseDto<?> createTripBookmark(@PathVariable Long id) {
        return tripBookmarkService.createTripBookmark(id);
    }





}
