package com.innocamp.dduha.domain.member.service;

import com.innocamp.dduha.domain.member.dto.request.PasswordRequestDto;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.member.dto.request.EmailRequestDto;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.member.model.PasswordEncode;
import com.innocamp.dduha.domain.member.repository.MemberRepository;
import com.innocamp.dduha.domain.member.repository.PasswordEncodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final JavaMailSender emailSender;
    private final PasswordEncodeRepository passwordEncodeRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${naver.mail-address}")
    private String NaverMailAddress;

    @Value("${naver.mail-findPassword}")
    private String passwordURL;

    // 이메일 생성
    private MimeMessage createMessage(String to, String code) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);

        message.setSubject("뚜벅하우까 비밀번호 찾기");
        message.setText("비밀번호 재설정 링크 : " + passwordURL + code);

        message.setFrom(new InternetAddress(NaverMailAddress, "뚜벅하우까"));

        return message;
    }

    // RandomCode 생성
    public String getRandomCode() {
        char[] charSet = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        StringBuilder sb = new StringBuilder();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        int len = charSet.length;
        for (int i = 0; i < 10; i++) {
            int idx = sr.nextInt(len);
            sb.append(charSet[idx]);
        }
        return sb.toString();
    }

    // RandomCode DB에 저장
    public String saveCode(EmailRequestDto requestDto) {

        String code = getRandomCode();
        PasswordEncode passwordEncode = PasswordEncode.builder()
                .email(requestDto.getEmail())
                .randomCode(code)
                .build();
        passwordEncodeRepository.save(passwordEncode);

        return code;
    }

    // 이메일 전송
    public ResponseEntity<?> sendSimpleMessage(EmailRequestDto requestDto) throws Exception {
        memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(MEMBER_NOT_FOUND)));
        PasswordEncode passwordEncode = passwordEncodeRepository.findPasswordEncodeByEmail(requestDto.getEmail()).orElse(null);
        if (passwordEncode != null && passwordEncode.getCreatedAt().plusDays(1).isAfter(LocalDateTime.now())) {
            throw new ValidationException(String.valueOf(ALREADY_REQUESTED_EMAIL));
        } else if (passwordEncode != null) {
            passwordEncodeRepository.delete(passwordEncode);
        }
        String code = saveCode(requestDto);
        MimeMessage message = createMessage(requestDto.getEmail(), code);
        emailSender.send(message);

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    // 비밀번호 재설정
    @Transactional
    public ResponseEntity<?> resetPassword(PasswordRequestDto requestDto) {
        PasswordEncode passwordEncode = passwordEncodeRepository.findPasswordEncodeByRandomCode(requestDto.getCode()).orElseThrow(() ->
                new ValidationException(String.valueOf(INVALID_CODE)));
        if (passwordEncode.getCreatedAt().plusDays(1).isBefore(LocalDateTime.now())) {
            passwordEncodeRepository.delete(passwordEncode);
            throw new ValidationException(String.valueOf(EXPIRED_CODE));
        }
        String email = passwordEncode.getEmail();
        Member member = memberRepository.findMemberByEmail(email);

        if (member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            throw new ValidationException(String.valueOf(USED_PASSWORD));
        }

        member.resetPassword(passwordEncoder, requestDto.getPassword());
        memberRepository.save(member);

        passwordEncodeRepository.delete(passwordEncode);

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

}
