package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.request.ReviewRequestDto;
import com.innocamp.dduha.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/auth/{category}/review/{id}")
    public ResponseEntity<?> createReview(@PathVariable String category, @PathVariable Long id,
                                          @RequestBody ReviewRequestDto requestDto) {
        return reviewService.createReview(category, id, requestDto);
    }

    @PutMapping("/auth/{category}/review/{id}")
    public ResponseEntity<?> updateReview(@PathVariable String category, @PathVariable Long id,
                                       @RequestBody ReviewRequestDto requestDto) throws AuthenticationException {
        return reviewService.updateReview(category, id, requestDto);
    }

    @DeleteMapping("/auth/{category}/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String category, @PathVariable Long id) throws AuthenticationException {
        return reviewService.deleteReview(category, id);
    }
}
