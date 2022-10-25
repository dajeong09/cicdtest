package com.innocamp.dduha.domain.review.repository;

import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
import com.innocamp.dduha.domain.review.model.TouristSpotReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotReviewRepository extends JpaRepository<TouristSpotReview, Long> {
    List<TouristSpotReview> findAllByTouristSpotOrderByCreatedAtDesc(TouristSpot touristSpot);
    List<TouristSpotReview> findAllByMember(Member member);
}
