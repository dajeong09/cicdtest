package com.innocamp.dduha.repository.touristspot;

import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.model.touristspot.TouristSpotImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotImgRepository extends JpaRepository<TouristSpotImg, Long> {
    List<TouristSpotImg> findAllByTouristSpot(TouristSpot touristSpot);
}
