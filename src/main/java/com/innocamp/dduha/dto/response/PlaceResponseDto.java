package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceResponseDto {
    private Long id;
    private String category;
    private String name;
    private String description;
    private int likeNum;
    private String region;
    private String thumbnailUrl;
    private boolean hasNearStation;
    private boolean isBookmarked;
}
