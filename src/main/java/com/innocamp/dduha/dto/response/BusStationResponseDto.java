package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusStationResponseDto {
    private String stationName;
    private int distance;
}
