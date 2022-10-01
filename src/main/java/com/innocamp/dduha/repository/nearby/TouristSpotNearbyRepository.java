package com.innocamp.dduha.repository.nearby;

import com.innocamp.dduha.model.nearby.TouristSpotNearby;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotNearbyRepository extends JpaRepository<TouristSpotNearby, Long> {
    List<TouristSpotNearby> findAllByTouristSpot(TouristSpot touristSpot);
}
