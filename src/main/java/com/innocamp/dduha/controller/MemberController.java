package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.request.MemberRequestDto;
import com.innocamp.dduha.dto.request.LoginRequestDto;
import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/member/signup")
    public ResponseDto<?> signup(@RequestBody MemberRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    // 이메일 중복 검사
    @PostMapping("/member/emailcheck")
    public ResponseDto<?> emailCheck(@RequestBody MemberRequestDto requestDto) {
        return memberService.emailCheck(requestDto);
    }

    // 닉네임 중복 검사
    @PostMapping("/member/nicknamecheck")
    public ResponseDto<?> nicknameCheck(@RequestBody MemberRequestDto requestDto) {
        return memberService.nicknameCheck(requestDto);
    }

    // 로그인
    @PostMapping("/member/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    // 로그 아웃
    @PostMapping("/auth/member/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

    // 카카오 로그인

    // 구글 로그인
}
