package com.innocamp.dduha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innocamp.dduha.dto.request.*;
import com.innocamp.dduha.service.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final EmailService emailService;
    private final PasswordService passwordService;

    // 회원가입
    @PostMapping("/member/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    // 로그인
    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    // 회원 정보 수정
    @PutMapping("/auth/member/modify")
    public ResponseEntity<?> modifyMember(@RequestBody ModifyMemberRequestDto requestDto, HttpServletResponse response) {
        return memberService.modifyMember(requestDto, response);
    }

    // 회원 탈퇴
    @DeleteMapping("/auth/member/delete")
    public ResponseEntity<?> deleteMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.deleteMember(requestDto);
    }

    // 카카오 로그인
    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam(name = "code") String code, HttpServletResponse response)
            throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }

    // 구글 로그인
    @GetMapping(value = "/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestParam(value = "code") String code, HttpServletResponse response) {
        return googleService.googleLogin(code, response);
    }

    // 이메일로 회원가입 링크 전송
    @PostMapping("/member/emailConfirm")
    public ResponseEntity<?> emailConfirm(@RequestBody EmailRequestDto requestDto) throws Exception {
        return emailService.sendSimpleMessage(requestDto);
    }

    // RandomCode를 이용해 이메일 확인
    @GetMapping("/member/email")
    public ResponseEntity<?> getEmail(@RequestParam(value = "code") String code) {
        return emailService.getEmail(code);
    }

    // 이메일로 비밀번호 재설정 링크 전송
    @PostMapping("/member/findPassword")
    public ResponseEntity<?> findPassword(@RequestBody EmailRequestDto requestDto) throws Exception {
        return passwordService.sendSimpleMessage(requestDto);
    }

    // 비밀번호 재설정
    @PostMapping("/member/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordRequestDto requestDto) {
        return passwordService.resetPassword(requestDto);
    }
}
