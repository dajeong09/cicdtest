package com.innocamp.dduha.domain.member.service;

import com.innocamp.dduha.domain.member.model.EmailEncode;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.member.dto.request.EmailRequestDto;
import com.innocamp.dduha.domain.member.dto.response.EmailResponseDto;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.member.repository.EmailEncodeRepository;
import com.innocamp.dduha.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.innocamp.dduha.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final EmailEncodeRepository emailEncodeRepository;
    private final MemberRepository memberRepository;

    @Value("${naver.mail-address}")
    private String NaverMailAddress;

    @Value("${naver.mail-signup}")
    private String signUpURL;

    // 이메일 생성
    private MimeMessage createMessage(String to, String code) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);

        message.setSubject("뚜벅하우까 회원가입");
        message.setText("회원가입 링크 : " + signUpURL + code);

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
        EmailEncode emailEncode = EmailEncode.builder()
                .email(requestDto.getEmail())
                .randomCode(code)
                .build();
        emailEncodeRepository.save(emailEncode);

        return code;
    }

    // 이메일 전송
    public ResponseEntity<?> sendSimpleMessage(EmailRequestDto requestDto) throws Exception {
        Optional<Member> optionalMember = memberRepository.findByEmail(requestDto.getEmail());
        if (optionalMember.isPresent()) {
            throw new ValidationException(String.valueOf(DUPLICATE_EMAIL));
        }
        EmailEncode emailEncode = emailEncodeRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (emailEncode != null && emailEncode.getCreatedAt().plusDays(1).isAfter(LocalDateTime.now())) {
            throw new ValidationException(String.valueOf(ALREADY_REQUESTED_EMAIL));
        } else if (emailEncode != null) {
            emailEncodeRepository.delete(emailEncode);
        }
        String code = saveCode(requestDto);
        MimeMessage message = createMessage(requestDto.getEmail(),code);
        emailSender.send(message);

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    // RandomCode를 이용해 이메일 주소 확인
    public ResponseEntity<?> getEmail(String code) {

        EmailEncode emailEncode = emailEncodeRepository.findByRandomCode(code).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(CODE_NOT_FOUND)));
        if (null == emailEncode) {
            throw new ValidationException(String.valueOf(INVALID_CODE));
        }

        if (emailEncode.getCreatedAt().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new ValidationException(String.valueOf(EXPIRED_CODE));
        }

        EmailResponseDto emailResponseDto = EmailResponseDto.builder()
                .email(emailEncode.getEmail())
                .build();

        return ResponseEntity.ok(ResponseDto.success(emailResponseDto));
    }

}
