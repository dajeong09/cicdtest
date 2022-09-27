package com.innocamp.dduha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OauthUserDto {
    private Long id;
    private String nickname;
    private String email;
}