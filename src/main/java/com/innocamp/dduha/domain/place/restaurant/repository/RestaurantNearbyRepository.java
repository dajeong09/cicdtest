package com.innocamp.dduha.domain.place.restaurant.repository;

import com.innocamp.dduha.domain.place.restaurant.model.RestaurantNearby;
import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantNearbyRepository extends JpaRepository<RestaurantNearby, Long> {
    List<RestaurantNearby> findAllByRestaurant(Restaurant restaurant);
}
