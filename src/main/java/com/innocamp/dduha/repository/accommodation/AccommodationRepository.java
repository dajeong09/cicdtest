package com.innocamp.dduha.repository.accommodation;

import com.innocamp.dduha.model.accommodation.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByNameContaining(String keyword);
    List<Accommodation> findByRegionAndNameContaining(String region,String keyword);
    List<Accommodation> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);
    List<Accommodation> findByRegionAndNameContainingOrRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2, String region3, String keyword3);
    Page<Accommodation> findByRegion(Pageable pageable, String region);
    Page<Accommodation> findByRegionOrRegionOrRegion(Pageable pageable, String region1, String region2, String region3);
    @Query("select a from Accommodation a where exists (select an from AccommodationNearby an where an.accommodation=a)")
    Page<Accommodation> findByHasStation(Pageable pageable);
    @Query("select a from Accommodation a where exists (select an from AccommodationNearby an where an.accommodation=a) and a.region in (:region1, :region2, :region3)")
    Page<Accommodation> findByHasStationAndRegion(Pageable pageable, String region1, String region2, String region3);
}
