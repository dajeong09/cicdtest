package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class ReviewResponseDto {
    private Long id;
    private Long itemId;
    private String reviewName;
    private String reviewer;
    private String review;
    private String reviewedAt;
}
