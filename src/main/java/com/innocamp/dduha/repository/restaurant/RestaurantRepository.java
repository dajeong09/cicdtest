package com.innocamp.dduha.repository.restaurant;

import com.innocamp.dduha.model.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findById(Long restId);

    List<Restaurant> findByNameContaining(String keyword);

    List<Restaurant> findByRegionAndNameContaining(String region,String keyword);

    List<Restaurant> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);

    List<Restaurant> findAllByLatitudeBetweenAndLongitudeBetween(double latitude1, double latitude2, double longitude1, double longitude2);

    Page<Restaurant> findByRegion(Pageable pageable, String region);
    Page<Restaurant> findByRegionOrRegion(Pageable pageable, String region1, String region2);
}
