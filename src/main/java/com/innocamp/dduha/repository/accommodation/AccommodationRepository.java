package com.innocamp.dduha.repository.accommodation;

import com.innocamp.dduha.model.accommodation.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    List<Accommodation> findByNameContaining(String keyword);

    List<Accommodation> findByRegionAndNameContaining(String region,String keyword);

    List<Accommodation> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);
    Page<Accommodation> findByRegion(Pageable pageable, String region);
    Page<Accommodation> findByRegionOrRegion(Pageable pageable, String region1, String region2);
}
