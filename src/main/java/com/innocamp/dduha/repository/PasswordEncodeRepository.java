package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.PasswordEncode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordEncodeRepository extends JpaRepository<PasswordEncode, Long> {
    PasswordEncode findPasswordEncodeByRandomCode(String code);
}
