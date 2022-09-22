package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMember(Member member);
}
