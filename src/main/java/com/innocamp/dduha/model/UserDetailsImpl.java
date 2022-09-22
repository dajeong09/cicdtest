package com.innocamp.dduha.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
UserDetails는 Spring Security에서 사용자 정보를 담는 인터페이스.
Spring Security 기본 UserDetails를 Customizing한 클래스.
*/
@Data // Token Provider와 연결됨.
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Member member;

    // 해당 유저 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_MEMBER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    // 비밀 번호
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // PK 값
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    // 계정 만료 여부
    /* true : 만료 안됨
     * false : 만료
    */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    /* true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    /* true : 만료 안됨
     * false : 만료
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용자 활성화 여부
    /* true : 활성화
     * false : 비활성화
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
