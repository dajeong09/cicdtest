package com.innocamp.dduha.repository.touristspot;

import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
    Optional<TouristSpot> findTouristSpotById(Long Id);
}
