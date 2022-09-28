package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TripResponseDto {
    private Long id;
    private String title;
    private Boolean isPublic;
    private String startAt;
    private String endAt;
    private boolean isbookmarked;
}
