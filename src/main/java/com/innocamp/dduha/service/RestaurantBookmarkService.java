package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.RESTAURANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RestaurantBookmarkService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createRestaurantBookmark(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Restaurant restaurant = isPresentRestaurant(id);
        if (null == restaurant) {
            return ResponseDto.fail(RESTAURANT_NOT_FOUND);
        }
        RestaurantBookmark checkBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
        if (null != checkBookmark) {
            restaurantBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build());
        }
        RestaurantBookmark restaurantBookmark = RestaurantBookmark.builder()
                .member(member)
                .restaurant(restaurant)
                .build();

        restaurantBookmarkRepository.save(restaurantBookmark); // 즐겨 찾기
        return ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build());
    }

    @Transactional
    public Restaurant isPresentRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }
}
