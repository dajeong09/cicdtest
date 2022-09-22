package com.innocamp.dduha.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TripRequestDto {
    private String title;
    private Boolean isPublic;
    private String startAt;
    private String endAt;
}
