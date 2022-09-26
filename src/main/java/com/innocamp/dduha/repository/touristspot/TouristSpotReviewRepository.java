package com.innocamp.dduha.repository.touristspot;

import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.model.touristspot.TouristSpotReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotReviewRepository extends JpaRepository<TouristSpotReview, Long> {
    List<TouristSpotReview> findAllByTouristSpotOrderByReviewedAtDesc(TouristSpot touristSpot);
}
