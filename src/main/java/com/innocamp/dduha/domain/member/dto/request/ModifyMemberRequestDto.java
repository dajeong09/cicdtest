package com.innocamp.dduha.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMemberRequestDto {
    private String nickname;
    private String currentPassword;
    private String newPassword;
}
