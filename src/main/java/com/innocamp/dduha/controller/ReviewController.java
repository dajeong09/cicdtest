package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.ReviewRequestDto;
import com.innocamp.dduha.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/auth/{category}/review/{id}")
    public ResponseDto<?> createReview(@PathVariable String category, @PathVariable Long id, @RequestBody ReviewRequestDto requestDto) {
        return reviewService.createReview(category, id, requestDto);
    }

    @PutMapping("/auth/{category}/review/{id}")
    public ResponseDto<?> updateReview(@PathVariable String category, @PathVariable Long id, @RequestBody ReviewRequestDto requestDto) {
        return reviewService.updateReview(category, id, requestDto);
    }

    @DeleteMapping("/auth/{category}/review/{id}")
    public ResponseDto<?> deleteReview(@PathVariable String category, @PathVariable Long id) {
        return reviewService.deleteReview(category, id);
    }
}
