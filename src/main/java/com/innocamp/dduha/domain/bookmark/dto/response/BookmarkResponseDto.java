package com.innocamp.dduha.domain.bookmark.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkResponseDto {
    private boolean isBookmarked;
}
