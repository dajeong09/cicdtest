package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CourseNearbyResponseDto {
    private Long id;
    private String category;
    private String name;
    private String description;
    private int likeNum;
    private String region;
    private String thumbnailUrl;
    private boolean isBookmarked;
    private int distance;
}
