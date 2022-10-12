package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TideDetailResponseDto {
    private String level;
    private String time;
    private String code;
}
