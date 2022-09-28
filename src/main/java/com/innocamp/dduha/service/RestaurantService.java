package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.RestaurantResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final TokenProvider tokenProvider;

    private final RestaurantBookmarkRepository restaurantBookmarkRepository;

    public ResponseDto<?> getRestaurantList(HttpServletRequest request) {

        // 사용자 검증 추가 필요
        Member member = tokenProvider.getMemberFromAuthentication();

        List<Restaurant> restaurantList = restaurantRepository.findAll();
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();

        if (null != member) {
            for (Restaurant restaurant : restaurantList) {
                boolean isBookmarked = false;
                RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
                if (null != findRestaurantBookmark) {
                    isBookmarked = true;
                }
                restaurantResponseDtoList.add(
                        RestaurantResponseDto.builder()
                                .id(restaurant.getId())
                                .name(restaurant.getName())
                                .description(restaurant.getDescription())
                                .likeNum(restaurant.getLikeNum())
                                .region(restaurant.getRegion())
                                .thumbnailUrl(restaurant.getThumbnailUrl())
                                .isBookmarked(isBookmarked)
                                .build()
                );
            }
        } else {
            for (Restaurant restaurant : restaurantList) {
                restaurantResponseDtoList.add(
                        RestaurantResponseDto.builder()
                                .id(restaurant.getId())
                                .name(restaurant.getName())
                                .description(restaurant.getDescription())
                                .likeNum(restaurant.getLikeNum())
                                .region(restaurant.getRegion())
                                .thumbnailUrl(restaurant.getThumbnailUrl())
                                .build()
                );
            }
        }
        return ResponseDto.success(restaurantResponseDtoList);
    }
}
