package com.innocamp.dduha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innocamp.dduha.config.GoogleLoginConfig;
import com.innocamp.dduha.dto.request.MemberRequestDto;
import com.innocamp.dduha.dto.request.LoginRequestDto;
import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.KakaoService;
import com.innocamp.dduha.service.MemberService;
import com.innocamp.dduha.service.GoogleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    private final KakaoService kakaoService;

    private final GoogleLoginConfig configUtils;

    private final GoogleService googleService;

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
    @GetMapping("/oauth/kakao")
    public ResponseDto<?> kakaoLogin(@RequestParam(name = "code") String code, HttpServletResponse response)
            throws JsonProcessingException {
        //authorizeCode : 카카오 서버로부터 받은 인가 코드
        return kakaoService.kakaoLogin(code, response);
    }

    // 카카오 로그 아웃
    @GetMapping("/ouath/kakao/logout")
    public ResponseDto<?> kakaoLogout(HttpServletRequest request) {
        return kakaoService.logout(request);
    }

    // 구글 로그인
    @GetMapping(value = "/login/google")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        String authUrl = configUtils.googleInitUrl();
        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/auth/google")
    public ResponseDto<?> googleLogin(@RequestParam(value = "code") String code,
                                              HttpServletResponse response) {
        return googleService.googleLogin(code, response);
    }

}
