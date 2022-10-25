package com.innocamp.dduha.domain.mypage.dto.response;


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
