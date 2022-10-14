package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TideResponseDto {
    private String observatory;
    private String date;
    private List<TideDetailResponseDto> tide;
}
