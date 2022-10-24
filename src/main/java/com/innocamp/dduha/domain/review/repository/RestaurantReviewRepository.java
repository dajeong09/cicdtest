package com.innocamp.dduha.domain.review.repository;

import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import com.innocamp.dduha.domain.review.model.RestaurantReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantReviewRepository extends JpaRepository<RestaurantReview, Long> {
    List<RestaurantReview> findAllByRestaurantOrderByCreatedAtDesc(Restaurant restaurant);
    List<RestaurantReview> findAllByMember(Member member);
}
