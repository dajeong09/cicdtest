package com.innocamp.dduha.repository.restaurant;

import com.innocamp.dduha.model.restaurant.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
}
