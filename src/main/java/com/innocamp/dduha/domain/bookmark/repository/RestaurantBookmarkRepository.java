package com.innocamp.dduha.domain.bookmark.repository;

import com.innocamp.dduha.domain.bookmark.model.RestaurantBookmark;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantBookmarkRepository extends JpaRepository<RestaurantBookmark, Long> {
    RestaurantBookmark findByMemberAndRestaurant(Member member, Restaurant restaurant);
    int countRestaurantBookmarkByMember(Member member);
    List<RestaurantBookmark> findAllByMember(Member member);
    int countByRestaurant(Restaurant restaurant);
}



