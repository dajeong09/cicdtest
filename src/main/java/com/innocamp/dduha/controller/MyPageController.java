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

    // 내가 즐겨찾기한 각 목록 조회
    @GetMapping("/auth/mypage")
    public ResponseDto<?> getMyBookmarkedList(HttpServletRequest request) {
        return myPageService.getMyBookmarkedList(request);
    }

}
