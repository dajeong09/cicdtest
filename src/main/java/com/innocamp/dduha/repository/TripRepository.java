package com.innocamp.dduha.repository;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByMember(Member member);

}
