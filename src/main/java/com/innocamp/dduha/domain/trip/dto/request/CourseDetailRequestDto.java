package com.innocamp.dduha.domain.trip.dto.request;

import lombok.Getter;

@Getter
public class CourseDetailRequestDto {
    private Long courseId;
    private int detailOrder;
    private String category;
    private Long detailId;
}
