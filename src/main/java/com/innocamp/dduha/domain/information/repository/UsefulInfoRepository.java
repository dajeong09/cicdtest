package com.innocamp.dduha.domain.information.repository;

import com.innocamp.dduha.domain.information.model.UsefulInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsefulInfoRepository extends JpaRepository<UsefulInfo, Long> {
    List<UsefulInfo> findAllByCategory(String category);
}
