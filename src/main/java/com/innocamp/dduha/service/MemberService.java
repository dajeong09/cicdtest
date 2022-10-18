package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.LoginRequestDto;
import com.innocamp.dduha.dto.request.MemberRequestDto;
import com.innocamp.dduha.dto.request.ModifyMemberRequestDto;
import com.innocamp.dduha.jwt.TokenDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Authority;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.repository.EmailEncodeRepository;
import com.innocamp.dduha.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailEncodeRepository emailEncodeRepository;


    //회원 가입
    @Transactional
    public ResponseDto<?> signup(MemberRequestDto requestDto) {

        Member member = isPresentMemberByNickname(requestDto.getNickname());
        if (null != member) {
            return ResponseDto.fail(DUPLICATE_NICKNAME);
        }

        member = Member.builder()
                .email(requestDto.getEmail())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .authority(Authority.ROLE_MEMBER)
                .build();

        memberRepository.save(member);

        emailEncodeRepository.deleteByEmail(requestDto.getEmail());

        return ResponseDto.success(NULL);

    }


    // 로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMemberByEmail(requestDto.getEmail());
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail(INVALID_PASSWORD);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseDto.success(NULL);

    }

    // 회원 정보 수정
    @Transactional
    public ResponseDto<?> modifyMember(ModifyMemberRequestDto requestDto, HttpServletResponse response) {

        Member member = tokenProvider.getMemberFromAuthentication();

        String password = member.getPassword();
        String currentPassword = requestDto.getCurrentPassword();
        if (currentPassword != null) {
            if (!member.validatePassword(passwordEncoder, currentPassword)) {
                return ResponseDto.fail(INVALID_PASSWORD);
            }
            if (member.getPassword().equals(requestDto.getNewPassword())) {
                return ResponseDto.fail(USED_PASSWORD);
            }
            password = passwordEncoder.encode(requestDto.getNewPassword());
        }

        member.modify(requestDto.getNickname(), password);
        memberRepository.save(member);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseDto.success(NULL);
    }

    // 회원 탈퇴
    @Transactional
    public ResponseDto<?> deleteMember(MemberRequestDto requestDto) {

        Member member = tokenProvider.getMemberFromAuthentication();

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail(INVALID_PASSWORD);
        }

        memberRepository.delete(member);
        return ResponseDto.success(NULL);
    }

    @Transactional
    public Member isPresentMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    @Transactional
    public Member isPresentMemberByNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

}
