package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InfoResponseDto {
    private String region;
    private String name;
    private String address;
}
