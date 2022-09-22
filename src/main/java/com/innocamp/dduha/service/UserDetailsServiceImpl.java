package com.innocamp.dduha.service;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.UserDetailsImpl;
import com.innocamp.dduha.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
* UserDetailService는 Spring Security에서 유저 정보를 가져오는 인터페이스이다.
* loadUserByUsermame 메서드를 오버라이드 하여 사용한다.
* 스프링 시큐리티가 랜덤생성해주는 유저 말고, 우리가 원하는 유저로 인증한다.
*/
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(email);
        return new UserDetailsImpl(member.orElse(null));
    }
}