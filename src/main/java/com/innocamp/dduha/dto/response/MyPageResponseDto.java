package com.innocamp.dduha.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseDto {
    private int tripBookmarkNum;
    private int touristSpotBookmarkNum;
    private int restaurantBookmarkNum;
    private int accommodationBookmarkNum;
}
