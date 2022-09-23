package com.innocamp.dduha.dto.request;

import lombok.Getter;

@Getter
public class TripRequestDto {
    private String title;
    private Boolean isPublic;
    private String startAt;
    private String endAt;
}
