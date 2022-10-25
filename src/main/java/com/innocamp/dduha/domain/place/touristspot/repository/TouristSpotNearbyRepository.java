package com.innocamp.dduha.domain.place.touristspot.repository;

import com.innocamp.dduha.domain.place.touristspot.model.TouristSpotNearby;
import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotNearbyRepository extends JpaRepository<TouristSpotNearby, Long> {
    List<TouristSpotNearby> findAllByTouristSpot(TouristSpot touristSpot);
}
