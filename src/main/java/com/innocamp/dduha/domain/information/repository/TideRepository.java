package com.innocamp.dduha.domain.information.repository;

import com.innocamp.dduha.domain.information.model.Tide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TideRepository extends JpaRepository<Tide, Long> {
    List<Tide> findAllByDateAndObservatory(LocalDate date, String obs);
}
