package com.innocamp.dduha.domain.place.general.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListResponseDto<T> {
    private int totalPages;
    private int nextPage;
    private T list;
}
