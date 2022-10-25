package com.innocamp.dduha.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordResponseDto {
    private String email;
}
