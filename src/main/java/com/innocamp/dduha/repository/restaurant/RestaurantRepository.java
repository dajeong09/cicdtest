package com.innocamp.dduha.repository.restaurant;

import com.innocamp.dduha.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
