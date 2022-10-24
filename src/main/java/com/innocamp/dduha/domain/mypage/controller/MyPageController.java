package com.innocamp.dduha.domain.mypage.controller;

import com.innocamp.dduha.domain.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    // 내가 즐겨찾기한 각 목록 조회(즐겨찾기 개수)
    @GetMapping("/auth/mypage")
    public ResponseEntity<?> getMyBookmarkedList() {
        return myPageService.getMyBookmarkedList();
    }

    // 내가 즐겨찾기한 관광지 조회
    @GetMapping("/auth/mypage/touristspot/bookmark")
    public ResponseEntity<?> getMyTouristSpotBookmark() {
        return myPageService.getMyTouristSpotBookmark();
    }

    //내가 즐겨찾기한 맛집 조회
    @GetMapping("/auth/mypage/restaurant/bookmark")
    public ResponseEntity<?> getMyRestaurantBookmark() {
        return myPageService.getMyRestaurantBookmark();
    }

    //내가 즐겨찾기한 숙소 조회
    @GetMapping("/auth/mypage/accommodation/bookmark")
    public ResponseEntity<?> getMyAccommodationBookmark() {
        return myPageService.getMyAccommodationBookmark();
    }

    //내가 즐겨찾기한 일정 조회
    @GetMapping("/auth/mypage/trip/bookmark")
    public ResponseEntity<?> getMyTripBookmark() {
        return myPageService.getMyTripBookmark();
    }

    // 내가 작성한 관광지 댓글 조회
    @GetMapping("/auth/mypage/touristspotreview")
    public ResponseEntity<?> getMyTouristSpotReview() {
        return myPageService.getMyTouristSpotReview();
    }

    // 내가 작성한 맛집 댓글 조회
    @GetMapping("/auth/mypage/restaurantreview")
    public ResponseEntity<?> getMyRestaurantReview() {
        return myPageService.getMyRestaurantReview();
    }

    // 내가 작성한 숙소 댓글 조회
    @GetMapping("/auth/mypage/accommodationreview")
    public ResponseEntity<?> getMyAccommodationReview() {
        return myPageService.getMyAccommodationReview();
    }

}
