package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.PasswordEncode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordEncodeRepository extends JpaRepository<PasswordEncode, Long> {
    Optional<PasswordEncode> findPasswordEncodeByRandomCode(String code);
    Optional<PasswordEncode> findPasswordEncodeByEmail(String email);
}
