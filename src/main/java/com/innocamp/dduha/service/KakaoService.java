package com.innocamp.dduha.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocamp.dduha.dto.OauthUserDto;
import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.jwt.TokenDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Authority;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.repository.MemberRepository;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.innocamp.dduha.exception.ErrorCode.NULL;


@Service
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    @Value("${kakao.client-id}")
    private String KakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String KakaoRedirectUri;

    public ResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String kakaoToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        OauthUserDto kakaoUserInfo = getKakaoUserInfo(kakaoToken);

        Member member = memberRepository.findMemberByEmail(kakaoUserInfo.getEmail());

        String kNickname = kakaoUserInfo.getNickname()+"_k1";
        while (memberRepository.findByNickname(kNickname).isPresent()) {
            String[] strings = kNickname.split("_k");
            kNickname = strings[0]+"_k"+ (Integer.parseInt(strings[1])+1);
        }

        // 3. 처음 접속한 회원일 경우, 회원 가입 요청
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
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseDto.success(NULL);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        // HttpEntity가 MultiValue로 받겠다고 선언해 놓음
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        // 본인 REST API 키
        body.add("client_id", KakaoClientId);
        // controller에 설정한 uri 입력
        body.add("redirect_uri", KakaoRedirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        // 카카오가 지정한 방식
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        // RestTemplate은 동기적으로 API 요청을 보낸다
        // exchange로 요청을 보내고, 요청 받은 서버에서 return 값 올 때까지 기다린다
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                // Token이 String 타입이므로
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        // ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // JsonNode 중첩된 Json
        //readTree 역직렬화, 객체형으로 다시 바꿔준다
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private OauthUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
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


        System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new OauthUserDto(id, nickname, email);
    }

    // 로그 아웃
    public ResponseDto<?> logout(HttpServletRequest request) {
        String token = request.getHeader("kakaoToken");

        String reqURL = "http://localhost:8080/ouath/kakao/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseDto.success(NULL);
    }

}
