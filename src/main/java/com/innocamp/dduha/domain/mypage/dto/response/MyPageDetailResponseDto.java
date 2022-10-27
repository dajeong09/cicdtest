package com.innocamp.dduha.domain.mypage.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MyPageDetailResponseDto {
    private Long id;
    private String name;
    private String description;
    private int bookmarkNum;
    private String region;
    private String thumbnailUrl;
    private boolean hasNearStation;
    private boolean isBookmarked;
}
