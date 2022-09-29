package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    // 내가 즐겨찾기한 각 목록 조회(즐겨찾기 개수)
    @GetMapping("/auth/mypage")
    public ResponseDto<?> getMyBookmarkedList(HttpServletRequest request) {
        return myPageService.getMyBookmarkedList(request);
    }

    // 내가 즐겨찾기한 관광지 조회
    @GetMapping("/auth/mypage/touristspot/bookmark")
    public ResponseDto<?> getMyTouristSpotBookmark(HttpServletRequest request) {
        return myPageService.getMyTouristSpotBookmark(request);
    }

    //내가 즐겨찾기한 맛집 조회
    @GetMapping("/auth/mypage/restaurant/bookmark")
    public ResponseDto<?> getMyRestaurantBookmark(HttpServletRequest request) {
        return myPageService.getMyRestaurantBookmark(request);
    }

    //내가 즐겨찾기한 숙소 조회

    //내가 즐겨찾기한 일정 조회
    @GetMapping("/auth/mypage/trip/bookmark")
    public ResponseDto<?> getMyTripBookmark(HttpServletRequest request) {
        return myPageService.getMyTripBookmark(request);
    }

}
