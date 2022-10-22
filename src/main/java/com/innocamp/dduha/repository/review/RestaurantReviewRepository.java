package com.innocamp.dduha.repository.review;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.review.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
    List<RestaurantReview> findAllByRestaurantOrderByCreatedAtDesc(Restaurant restaurant);
    List<RestaurantReview> findAllByMember(Member member);
}
