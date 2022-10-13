package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.EmailEncode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailEncodeRepository extends JpaRepository<EmailEncode, Long> {
   EmailEncode findEmailEncodeByRandomCode(String code);
}
