package com.innocamp.dduha.repository.nearby;

import com.innocamp.dduha.model.nearby.RestaurantNearby;
import com.innocamp.dduha.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantNearbyRepository extends JpaRepository<RestaurantNearby, Long> {
    List<RestaurantNearby> findAllByRestaurant(Restaurant restaurant);
}
