package com.innocamp.dduha.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseRequestDto {
    private Long courseId;
    private List<CourseDetailRequestDto> courseDetails;
}
