package com.innocamp.dduha.domain.place.restaurant.repository;

import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import com.innocamp.dduha.domain.place.restaurant.model.RestaurantImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantImgRepository extends JpaRepository<RestaurantImg, Long> {
    List<RestaurantImg> findAllByRestaurant(Restaurant restaurant);
}
