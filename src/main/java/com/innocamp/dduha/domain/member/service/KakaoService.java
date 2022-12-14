package com.innocamp.dduha.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocamp.dduha.domain.member.dto.KaKaoLoginDto;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.global.common.TokenDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Authority;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;

import static com.innocamp.dduha.global.exception.ErrorCode.NULL;


@Service
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    @Value("${kakao.client-id}")
    private String KakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String KakaoRedirectUri;

    public ResponseEntity<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        String kakaoToken = getAccessToken(code);

        KaKaoLoginDto kakaoUserInfo = getKakaoUserInfo(kakaoToken);

        Member member = memberRepository.findMemberByEmail(kakaoUserInfo.getEmail());

        String kNickname = kakaoUserInfo.getNickname()+"_k1";
        while (memberRepository.findByNickname(kNickname).isPresent()) {
            String[] strings = kNickname.split("_k");
            kNickname = strings[0]+"_k"+ (Integer.parseInt(strings[1])+1);
        }

        if (member == null) {
            member = Member.builder()
                    .nickname(kNickname)
                    .email(kakaoUserInfo.getEmail())
                    .password("7ZqM7JuQ6rCA7J6F65qc67KF7ZWY7Jqw6rmM7J6F64uI64uk")
                    .authority(Authority.ROLE_MEMBER)
                    .provider("KAKAO")
                    .build();
            member = memberRepository.save(member);
        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body ??????
        // HttpEntity??? MultiValue??? ???????????? ????????? ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        // ?????? REST API ???
        body.add("client_id", KakaoClientId);
        // controller??? ????????? uri ??????
        body.add("redirect_uri", KakaoRedirectUri);
        body.add("code", code);

        // HTTP ?????? ?????????
        // ???????????? ????????? ??????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        // RestTemplate??? ??????????????? API ????????? ?????????
        // exchange??? ????????? ?????????, ?????? ?????? ???????????? return ??? ??? ????????? ????????????
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP ?????? (JSON) -> ????????? ?????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        // JsonNode ????????? Json
        //readTree ????????????, ??????????????? ?????? ????????????
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KaKaoLoginDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("kakao_account")
                .get("profile").get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KaKaoLoginDto(id, nickname, email);
    }

}
