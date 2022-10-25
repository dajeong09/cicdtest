package com.innocamp.dduha.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoLoginDto {
    private Long id;
    private String nickname;
    private String email;
}