package com.innocamp.dduha.repository.touristspot;

import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TouristSpotRepository extends JpaRepository<TouristSpot, Long> {
    Optional<TouristSpot> findTouristSpotById(Long Id);

    List<TouristSpot> findByNameContaining(String keyword);

    List<TouristSpot> findByRegionAndNameContaining(String region,String keyword);

    List<TouristSpot> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);
    List<TouristSpot> findByRegionAndNameContainingOrRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2, String region3, String keyword3);

    List<TouristSpot> findAllByLatitudeBetweenAndLongitudeBetween(double latitude1, double latitude2, double longitude1, double longitude2);

    Page<TouristSpot> findByRegion(Pageable pageable, String region);

    Page<TouristSpot> findByRegionOrRegionOrRegion(Pageable pageable, String region1, String region2, String region3);

    @Query("select t from TouristSpot t where exists (select tn from TouristSpotNearby tn where tn.touristSpot=t)")
    Page<TouristSpot> findByHasStation(Pageable pageable);

    @Query("select t from TouristSpot t where exists (select tn from TouristSpotNearby tn where tn.touristSpot=t) and t.region in (:region1, :region2, :region3)")
    Page<TouristSpot> findByHasStationAndRegion(Pageable pageable, String region1, String region2, String region3);

}
