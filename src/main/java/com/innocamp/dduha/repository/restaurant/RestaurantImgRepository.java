package com.innocamp.dduha.repository.restaurant;

import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.restaurant.RestaurantImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantImgRepository extends JpaRepository<RestaurantImg, Long> {
    List<RestaurantImg> findAllByRestaurant(Restaurant restaurant);
}
