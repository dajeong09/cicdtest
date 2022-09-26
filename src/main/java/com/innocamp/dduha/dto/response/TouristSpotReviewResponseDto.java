package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class TouristSpotReviewResponseDto {
    private Long id;
    private String reviewer;
    private String review;
}
