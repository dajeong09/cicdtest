package com.innocamp.dduha.domain.trip.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseRequestDto {
    private Long courseId;
    private List<CourseDetailRequestDto> courseDetails;
}
