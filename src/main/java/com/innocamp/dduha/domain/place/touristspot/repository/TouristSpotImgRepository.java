package com.innocamp.dduha.domain.place.touristspot.repository;

import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
import com.innocamp.dduha.domain.place.touristspot.model.TouristSpotImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotImgRepository extends JpaRepository<TouristSpotImg, Long> {
    List<TouristSpotImg> findAllByTouristSpot(TouristSpot touristSpot);
}
