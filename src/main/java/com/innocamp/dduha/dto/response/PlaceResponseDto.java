package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlaceResponseDto {
    private Long id;
    private String category;
    private String name;
    private String description;
    private int bookmarkNum;
    private String region;
    private String address;
    private Double latitude;
    private Double longitude;
    private String thumbnailUrl;
    private boolean hasNearStation;
    private boolean isBookmarked;
    private LocalDateTime createdAt;
}
