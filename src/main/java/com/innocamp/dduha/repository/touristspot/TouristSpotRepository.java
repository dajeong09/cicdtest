package com.innocamp.dduha.repository.touristspot;

import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
    Optional<TouristSpot> findTouristSpotById(Long Id);

    List<TouristSpot> findByNameContaining(String keyword);

    List<TouristSpot> findByRegionAndNameContaining(String region,String keyword);

    List<TouristSpot> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);

    List<TouristSpot> findAllByLatitudeBetweenAndLongitudeBetween(double latitude1, double latitude2, double longitude1, double longitude2);

    Page<TouristSpot> findByRegion(Pageable pageable, String region);
    Page<TouristSpot> findByRegionOrRegion(Pageable pageable, String region1, String region2);
}
