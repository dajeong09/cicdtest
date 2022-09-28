package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.RestaurantResponseDto;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public ResponseDto<?> getRestaurantList() {

        // 사용자 검증 추가 필요


        List<Restaurant> restaurantList = restaurantRepository.findAll();
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();

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

        return ResponseDto.success(restaurantResponseDtoList);
    }



}
