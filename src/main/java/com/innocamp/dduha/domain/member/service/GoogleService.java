package com.innocamp.dduha.domain.member.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.innocamp.dduha.domain.member.dto.GoogleLoginDto;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.member.dto.request.GoogleLoginRequestDto;
import com.innocamp.dduha.domain.member.dto.response.GoogleLoginResponseDto;
import com.innocamp.dduha.global.common.TokenDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Authority;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import static com.innocamp.dduha.global.exception.ErrorCode.NULL;
import static com.innocamp.dduha.global.exception.ErrorCode.OAUTH_LOGIN_FAILED;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.secret}")
    private String googleSecret;
    @Value("${google.redirect.uri}")
    private String googleRedirectUrl;
    @Value("${google.auth.url}")
    private String googleAuthUrl;

    public ResponseEntity<?> googleLogin(String code, HttpServletResponse response) {

        GoogleLoginDto googleUser = findGoogleUser(code);

        Member member = memberRepository.findMemberByEmail(googleUser.getEmail());

        String[] str = googleUser.getEmail().split("@");
        String gNickname = str[0]+"_g1";
        while (memberRepository.findByNickname(gNickname).isPresent()) {
            String[] strings = gNickname.split("_g");
            gNickname = strings[0]+"_g"+ (Integer.parseInt(strings[1])+1);
        }

        if (member == null) {

            member = Member.builder()
                    .nickname(gNickname) // 구글에 닉네임 정보 없어서 대체함
                    .email(googleUser.getEmail())
                    .password("7ZqM7JuQ6rCA7J6F65qc67KF7ZWY7Jqw6rmM7J6F64uI64uk")
                    .authority(Authority.ROLE_MEMBER)
                    .provider("GOOGLE")
                    .build();
            member = memberRepository.save(member);
        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    public GoogleLoginDto findGoogleUser(String code) {
        // HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        GoogleLoginRequestDto requestParams = GoogleLoginRequestDto.builder()
                .clientId(googleClientId)
                .clientSecret(googleSecret)
                .code(code)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code")
                .build();
        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GoogleLoginRequestDto> httpRequestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(googleAuthUrl + "/token", httpRequestEntity, String.class);

            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponseDto googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<>() {
            });

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUrl + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if (resultJson != null) {
                return objectMapper.readValue(resultJson, new TypeReference<>() {});
            } else {
                throw new ValidationException(String.valueOf(OAUTH_LOGIN_FAILED));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
