package com.innocamp.dduha.domain.member.dto.request;

import lombok.Builder;

@Builder
public class GoogleLoginRequestDto {
    private String clientId;
    private String redirectUri;
    private String clientSecret;
    private String code;
    private String grantType;
}