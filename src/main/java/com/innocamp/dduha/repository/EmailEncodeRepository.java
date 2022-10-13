package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.EmailEncode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailEncodeRepository extends JpaRepository<EmailEncode, Long> {
   Optional<EmailEncode> findByRandomCode(String code);
   Optional<EmailEncode> findByEmail(String email);
   void deleteByEmail(String email);
}
