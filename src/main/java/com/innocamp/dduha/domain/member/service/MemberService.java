package com.innocamp.dduha.domain.member.service;

import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.member.dto.request.LoginRequestDto;
import com.innocamp.dduha.domain.member.dto.request.MemberRequestDto;
import com.innocamp.dduha.domain.member.dto.request.ModifyMemberRequestDto;
import com.innocamp.dduha.global.common.TokenDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Authority;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.member.repository.EmailEncodeRepository;
import com.innocamp.dduha.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailEncodeRepository emailEncodeRepository;


    //회원 가입
    @Transactional
    public ResponseEntity<?> signup(MemberRequestDto requestDto) {

        if (memberRepository.findByNickname(requestDto.getNickname()).isPresent()) {
            throw new ValidationException(String.valueOf(DUPLICATE_NICKNAME));
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .authority(Authority.ROLE_MEMBER)
                .build();

        memberRepository.save(member);

        emailEncodeRepository.deleteByEmail(requestDto.getEmail());

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    // 로그인
    @Transactional
    public ResponseEntity<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(MEMBER_NOT_FOUND)));

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            throw new AuthorizationServiceException(String.valueOf(INVALID_PASSWORD));
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    // 회원 정보 수정
    @Transactional
    public ResponseEntity<?> modifyMember(ModifyMemberRequestDto requestDto, HttpServletResponse response) {

        Member member = tokenProvider.getMemberFromAuthentication();

        String password = member.getPassword();
        String currentPassword = requestDto.getCurrentPassword();
        if (currentPassword != null) {
            if (!member.validatePassword(passwordEncoder, currentPassword)) {
                throw new AuthorizationServiceException(String.valueOf(INVALID_PASSWORD));
            }
            if (member.getPassword().equals(requestDto.getNewPassword())) {
                throw new ValidationException(String.valueOf(USED_PASSWORD));
            }
            password = passwordEncoder.encode(requestDto.getNewPassword());
        }

        member.modify(requestDto.getNickname(), password);
        memberRepository.save(member);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    // 회원 탈퇴
    @Transactional
    public ResponseEntity<?> deleteMember(MemberRequestDto requestDto) {

        Member member = tokenProvider.getMemberFromAuthentication();

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            throw new ValidationException(String.valueOf((INVALID_PASSWORD)));
        }

        memberRepository.delete(member);

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

}
