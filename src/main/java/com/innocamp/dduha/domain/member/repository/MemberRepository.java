package com.innocamp.dduha.domain.member.repository;

import com.innocamp.dduha.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Member findMemberByEmail(String email);
}

