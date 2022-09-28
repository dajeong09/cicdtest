package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.RestaurantBookmarkService;
import com.innocamp.dduha.service.TouristSpotBookmarkService;
import com.innocamp.dduha.service.TripBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final TouristSpotBookmarkService touristSpotBookmarkService;

    private final RestaurantBookmarkService restaurantBookmarkService;

    private final TripBookmarkService tripBookmarkService;

    // 관광지 즐겨찾기 / 취소하기
    @GetMapping("/auth/touristspot/bookmark/{spotId}")
    public ResponseDto<?> createTouristSpotBookmark(@PathVariable Long spotId, HttpServletRequest request) {
        return touristSpotBookmarkService.createTouristSpotBookmark(spotId, request);
    }

    // 맛집 즐겨찾기 / 취소하기
    @GetMapping("/auth/restaurant/bookmark/{restId}")
    public ResponseDto<?> createRestaurantBookmark(@PathVariable Long restId, HttpServletRequest request) {
        return restaurantBookmarkService.createRestaurantBookmark(restId, request);
    }

    // 숙소 즐겨찾기 / 취소하기

    // 일정 즐겨찾기 / 취소하기
    @GetMapping("/auth/trip/bookmark/{tripId}")
    public ResponseDto<?> createTripBookmark(@PathVariable Long tripId, HttpServletRequest request) {
        return tripBookmarkService.createTripBookmark(tripId, request);
    }





}
