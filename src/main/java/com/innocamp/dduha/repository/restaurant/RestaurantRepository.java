package com.innocamp.dduha.repository.restaurant;

import com.innocamp.dduha.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findById(Long restId);

    List<Restaurant> findByNameContaining(String keyword);

    List<Restaurant> findByRegionAndNameContaining(String region,String keyword);

    List<Restaurant> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);
}
