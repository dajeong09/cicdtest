package com.innocamp.dduha.repository.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantBookmarkRepository extends JpaRepository<RestaurantBookmark, Long> {
    RestaurantBookmark findByMemberAndRestaurant(Member member, Restaurant restaurant);

    int countRestaurantBookmarkByMember(Member member);

    List<RestaurantBookmark> findAllByMember(Member member);
}



