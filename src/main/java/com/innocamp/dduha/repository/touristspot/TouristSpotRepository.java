package com.innocamp.dduha.repository.touristspot;

import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
    TouristSpot findTouristSpotById(Long id);
}
