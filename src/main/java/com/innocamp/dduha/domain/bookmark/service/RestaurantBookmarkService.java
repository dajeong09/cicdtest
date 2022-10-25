package com.innocamp.dduha.domain.bookmark.service;

import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import com.innocamp.dduha.domain.place.restaurant.repository.RestaurantRepository;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.bookmark.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.bookmark.model.RestaurantBookmark;
import com.innocamp.dduha.domain.bookmark.repository.RestaurantBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.RESTAURANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RestaurantBookmarkService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseEntity<?> createRestaurantBookmark(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(RESTAURANT_NOT_FOUND)));

        RestaurantBookmark checkBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
        if (null != checkBookmark) {
            restaurantBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build()));
        }
        RestaurantBookmark restaurantBookmark = RestaurantBookmark.builder()
                .member(member)
                .restaurant(restaurant)
                .build();

        restaurantBookmarkRepository.save(restaurantBookmark); // 즐겨 찾기
        return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build()));
    }

}
