package com.innocamp.dduha.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocamp.dduha.dto.GoogleLoginDto;
import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.GoogleLoginRequestDto;
import com.innocamp.dduha.dto.response.GoogleLoginResponseDto;
import com.innocamp.dduha.jwt.TokenDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Authority;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.repository.MemberRepository;
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

    public ResponseDto<?> googleLogin(String code, HttpServletResponse response) {

        // code를 가지고 구글 유저 가져오기
        // 추후 리팩토링 예정
        GoogleLoginDto googleUser = findGoogleUser(code);

        Member member = memberRepository.findMemberByEmail(googleUser.getEmail());

        if (member == null){
            String[] str = googleUser.getEmail().split("@");
            member = Member.builder()
                    .nickname(str[0]) // 구글에 닉네임 정보 없어서 대체함
                    .email(googleUser.getEmail())
                    .password("7ZqM7JuQ6rCA7J6F65qc67KF7ZWY7Jqw6rmM7J6F64uI64uk")
                    .authority(Authority.ROLE_MEMBER)
                    .provider("GOOGLE")
                    .build();
            Member savedMember = memberRepository.save(member);
            return ResponseDto.success(savedMember.getId() + " ," + savedMember.getNickname()); // 다시 닉네임으로 넣어주기
        }else{
            TokenDto tokenDto = tokenProvider.generateTokenDto(member);
            response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
            response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
            response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

            return ResponseDto.success(tokenDto.getAccessToken());
        }
    }
    public GoogleLoginDto findGoogleUser(String code){
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
//            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            GoogleLoginResponseDto googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<>() {
            });

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUrl + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if(resultJson != null) {
               return objectMapper.readValue(resultJson, new TypeReference<>() {
                });

            }
            else {
                throw new Exception("Google OAuth failed!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
