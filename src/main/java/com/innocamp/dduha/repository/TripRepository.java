package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByMemberAndIsHiddenIsFalseOrderByCreatedAtDesc(Member member);
    Page<Trip> findByIsPublicIsTrueAndIsHiddenIsFalse(Pageable pageable);
    Optional<Trip> findTripByIdAndIsHiddenFalse(Long id);
}
