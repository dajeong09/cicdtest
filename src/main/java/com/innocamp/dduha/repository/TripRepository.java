package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByMemberAndIsHidden(Member member, Boolean isHidden);

    List<Trip> findAllByIsPublicAndIsHidden(Boolean isPublic, Boolean isHidden);

    Optional<Trip> findById(Long tripId);


}
