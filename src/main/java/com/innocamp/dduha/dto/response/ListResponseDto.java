package com.innocamp.dduha.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ListResponseDto<T> {
    private int totalPages;
    private T list;
}
