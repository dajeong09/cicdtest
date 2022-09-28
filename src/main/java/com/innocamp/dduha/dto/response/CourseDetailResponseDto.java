package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CourseDetailResponseDto {
    private int detailOrder;
    private String category;
    private Long id;
    private String name;
}
