package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.ReviewRequestDto;
import com.innocamp.dduha.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/auth/{category}/review/{id}")
    public ResponseDto<?> createReview(@PathVariable String category, @PathVariable Long id,
                                       @RequestBody ReviewRequestDto requestDto, HttpServletRequest request) {
        return reviewService.createReview(category, id, requestDto, request);
    }

    @PutMapping("/auth/{category}/review/{id}")
    public ResponseDto updateReview(@PathVariable String category, @PathVariable Long id,
                                    @RequestBody ReviewRequestDto requestDto, HttpServletRequest request) {
        return reviewService.updateReview(category, id, requestDto, request);
    }

    @DeleteMapping("/auth/{category}/review/{id}")
    public ResponseDto deleteeview(@PathVariable String category, @PathVariable Long id,
                                    HttpServletRequest request) {
        return reviewService.deleteReview(category, id, request);
    }
}
