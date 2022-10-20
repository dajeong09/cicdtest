package com.innocamp.dduha.repository.review;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.model.review.TouristSpotReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotReviewRepository extends JpaRepository<TouristSpotReview, Long> {
    List<TouristSpotReview> findAllByTouristSpotOrderByCreatedAtDesc(TouristSpot touristSpot);

    List<TouristSpotReview> findAllByMember(Member member);
}
