package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TouristSpotDetailResponseDto {
    private Long id;
    private String name;
    private String description;
    private int likeNum;
    private String phone;
    private String address;
    private String info;
    private String region;
    private String thumbnailUrl;
    private List<String> imgUrl;
    private List<TouristSpotReviewResponseDto> touristSpotReviews;
    private boolean isBookmarked;
}
