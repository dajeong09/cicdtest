package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.*;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.nearby.RestaurantNearby;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.restaurant.RestaurantImg;
import com.innocamp.dduha.model.review.RestaurantReview;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.nearby.RestaurantNearbyRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantImgRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import com.innocamp.dduha.repository.review.RestaurantReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;
    private final RestaurantImgRepository restaurantImgRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final RestaurantNearbyRepository restaurantNearbyRepository;

    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public ResponseDto<?> getRestaurantList(int page, String region, String station) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Sort sort = Sort.by("likeNum").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<Restaurant> restaurants;
        List<RestaurantResponseDto> restaurantResponseDtoList = new ArrayList<>();

        if (station == null) {
            restaurants = restaurantRepository.findAll(pageable);
            if (region != null) {
                if (region.equals("우도")) {
                    restaurants = restaurantRepository.findByRegionOrRegionOrRegion(pageable, "성산", "우도", "표선");
                } else if (region.equals("구좌")) {
                    restaurants = restaurantRepository.findByRegionOrRegionOrRegion(pageable, "구좌", "조천", "비어있음");
                } else if (region.equals("애월")) {
                    restaurants = restaurantRepository.findByRegionOrRegionOrRegion(pageable, "애월", "한림", "비어있음");
                } else {
                    restaurants = restaurantRepository.findByRegion(pageable, region);
                }
            }
        } else {
            restaurants = restaurantRepository.findByHasStation(pageable);
            if (region != null) {
                if (region.equals("우도")) {
                    restaurants = restaurantRepository.findByHasStationAndRegion(pageable, "성산", "우도", "표선");
                } else if (region.equals("구좌")) {
                    restaurants = restaurantRepository.findByHasStationAndRegion(pageable, "구좌", "조천", "비어있음");
                } else if (region.equals("애월")) {
                    restaurants = restaurantRepository.findByHasStationAndRegion(pageable, "애월", "한림", "비어있음");
                } else {
                    restaurants = restaurantRepository.findByHasStationAndRegion(pageable, region, "비어있음", "비어있음");
                }
            }
        }

        if (null != member) {
            for (Restaurant restaurant : restaurants) {
                boolean hasNearbyStation = false;
                boolean isBookmarked = false;
                List<RestaurantNearby> restaurantNearbyList = restaurantNearbyRepository.findAllByRestaurant(restaurant);
                if (restaurantNearbyList.size() > 0) {
                    hasNearbyStation = true;
                }
                RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
                if (null != findRestaurantBookmark) {
                    isBookmarked = true;
                }
                int bookmarkNum = restaurant.getLikeNum() + restaurantBookmarkRepository.countByRestaurant(restaurant);
                restaurantResponseDtoList.add(
                        RestaurantResponseDto.builder()
                                .id(restaurant.getId())
                                .name(restaurant.getName())
                                .description(restaurant.getDescription())
                                .bookmarkNum(bookmarkNum)
                                .region(restaurant.getRegion())
                                .thumbnailUrl(restaurant.getThumbnailUrl())
                                .hasNearStation(hasNearbyStation)
                                .isBookmarked(isBookmarked)
                                .build()
                );
            }
        } else {
            for (Restaurant restaurant : restaurants) {
                boolean hasNearbyStation = false;
                List<RestaurantNearby> restaurantNearbyList = restaurantNearbyRepository.findAllByRestaurant(restaurant);
                if (restaurantNearbyList.size() > 0) {
                    hasNearbyStation = true;
                }
                int bookmarkNum = restaurant.getLikeNum() + restaurantBookmarkRepository.countByRestaurant(restaurant);
                restaurantResponseDtoList.add(
                        RestaurantResponseDto.builder()
                                .id(restaurant.getId())
                                .name(restaurant.getName())
                                .description(restaurant.getDescription())
                                .bookmarkNum(bookmarkNum)
                                .region(restaurant.getRegion())
                                .thumbnailUrl(restaurant.getThumbnailUrl())
                                .hasNearStation(hasNearbyStation)
                                .build()
                );
            }
        }

        ListResponseDto listResponseDto = ListResponseDto.builder()
                .totalPages(restaurants.getTotalPages())
                .list(restaurantResponseDtoList).build();

        return ResponseDto.success(listResponseDto);
    }


    public ResponseDto<?> getRestaurantDetail(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Restaurant restaurant = isPresentRestaurant(id);
        List<RestaurantImg> restaurantImgList = restaurantImgRepository.findAllByRestaurant(restaurant);
        List<String> restaurantImgs = new ArrayList<>();
        for (RestaurantImg restaurantImg : restaurantImgList) {
            restaurantImgs.add(restaurantImg.getImgUrl());
        }

        List<RestaurantNearby> restaurantNearbyList = restaurantNearbyRepository.findAllByRestaurant(restaurant);
        List<BusStationResponseDto> busStationResponseDtoList = new ArrayList<>();
        for (RestaurantNearby restaurantNearby : restaurantNearbyList) {
            busStationResponseDtoList.add(
                    BusStationResponseDto.builder()
                            .stationName(restaurantNearby.getBusStation().getStationName())
                            .distance(restaurantNearby.getDistance()).build()
            );
        }

        List<RestaurantReview> restaurantReviewList = restaurantReviewRepository.findAllByRestaurantOrderByCreatedAtDesc(restaurant);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (RestaurantReview restaurantReview : restaurantReviewList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .id(restaurantReview.getId())
                            .reviewer(restaurantReview.getMember().getNickname())
                            .review(restaurantReview.getReview())
                            .reviewedAt(restaurantReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).build()
            );
        }

        int bookmarkNum = restaurant.getLikeNum() + restaurantBookmarkRepository.countByRestaurant(restaurant);

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
                    .bookmarkNum(bookmarkNum)
                    .thumbnailUrl(restaurant.getThumbnailUrl())
                    .region(restaurant.getRegion())
                    .latitude(restaurant.getLatitude())
                    .longitude(restaurant.getLongitude())
                    .stations(busStationResponseDtoList)
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
                    .bookmarkNum(bookmarkNum)
                    .thumbnailUrl(restaurant.getThumbnailUrl())
                    .region(restaurant.getRegion())
                    .latitude(restaurant.getLatitude())
                    .longitude(restaurant.getLongitude())
                    .stations(busStationResponseDtoList)
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

