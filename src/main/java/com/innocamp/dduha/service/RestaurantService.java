package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.RestaurantResponseDto;
import com.innocamp.dduha.dto.response.ReviewResponseDto;
import com.innocamp.dduha.dto.response.DetailResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.restaurant.RestaurantImg;
import com.innocamp.dduha.model.restaurant.RestaurantReview;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantImgRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;
    private final RestaurantImgRepository restaurantImgRepository;
    private final TokenProvider tokenProvider;

    private final RestaurantBookmarkRepository restaurantBookmarkRepository;

    public ResponseDto<?> getRestaurantList() {

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


    public ResponseDto<?> getRestaurantDetail(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Restaurant restaurant = isPresentRestaurant(id);
        List<RestaurantImg> restaurantImgList = restaurantImgRepository.findAllByRestaurant(restaurant);
        List<String> restaurantImgs = new ArrayList<>();
        for (RestaurantImg restaurantImg : restaurantImgList) {
            restaurantImgs.add(restaurantImg.getImgUrl());
        }
        List<RestaurantReview> restaurantReviewList = restaurantReviewRepository.findAllByRestaurantOrderByReviewedAtDesc(restaurant);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (RestaurantReview restaurantReview : restaurantReviewList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .id(restaurantReview.getId())
                            .reviewer(restaurantReview.getReviewer())
                            .review(restaurantReview.getReview()).build()
            );
        }

        DetailResponseDto responseDto;
        if (null != member) {
            boolean isBookmarked = false;
            RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
            if (null != findRestaurantBookmark) {
                isBookmarked = true;
            }
            responseDto = DetailResponseDto.builder()
                    .id(restaurant.getId())
                    .name(restaurant.getName())
                    .description(restaurant.getDescription())
                    .address(restaurant.getAddress())
                    .phone(restaurant.getPhone())
                    .info(restaurant.getInfo())
                    .likeNum(restaurant.getLikeNum())
                    .thumbnailUrl(restaurant.getThumbnailUrl())
                    .region(restaurant.getRegion())
                    .imgUrl(restaurantImgs)
                    .reviews(reviewResponseDtoList)
                    .isBookmarked(isBookmarked)
                    .build();

        } else {
            responseDto = DetailResponseDto.builder()
                    .id(restaurant.getId())
                    .name(restaurant.getName())
                    .description(restaurant.getDescription())
                    .address(restaurant.getAddress())
                    .phone(restaurant.getPhone())
                    .info(restaurant.getInfo())
                    .likeNum(restaurant.getLikeNum())
                    .thumbnailUrl(restaurant.getThumbnailUrl())
                    .region(restaurant.getRegion())
                    .imgUrl(restaurantImgs)
                    .reviews(reviewResponseDtoList)
                    .build();
        }
        return ResponseDto.success(responseDto);
    }

    @Transactional
    public Restaurant isPresentRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }

}

