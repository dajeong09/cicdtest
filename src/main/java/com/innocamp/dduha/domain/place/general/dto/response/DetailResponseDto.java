package com.innocamp.dduha.domain.place.general.dto.response;

import com.innocamp.dduha.domain.place.bustation.dto.response.BusStationResponseDto;
import com.innocamp.dduha.domain.review.dto.response.ReviewResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DetailResponseDto {
    private Long id;
    private String name;
    private String description;
    private int bookmarkNum;
    private String phone;
    private String address;
    private String info;
    private String region;
    private Double latitude;
    private Double longitude;
    private String thumbnailUrl;
    private List<String> imgUrl;
    private List<ReviewResponseDto> reviews;
    private List<BusStationResponseDto> stations;
    private boolean isBookmarked;
}
