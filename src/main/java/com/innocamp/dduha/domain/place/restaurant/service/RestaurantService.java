package com.innocamp.dduha.domain.place.restaurant.service;

import com.innocamp.dduha.domain.bookmark.model.RestaurantBookmark;
import com.innocamp.dduha.domain.bookmark.repository.RestaurantBookmarkRepository;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.bustation.dto.response.BusStationResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.DetailResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.ListResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.PlaceResponseDto;
import com.innocamp.dduha.domain.place.restaurant.model.RestaurantNearby;
import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import com.innocamp.dduha.domain.place.restaurant.model.RestaurantImg;
import com.innocamp.dduha.domain.review.dto.response.ReviewResponseDto;
import com.innocamp.dduha.domain.review.model.RestaurantReview;
import com.innocamp.dduha.domain.review.repository.RestaurantReviewRepository;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.place.restaurant.repository.RestaurantNearbyRepository;
import com.innocamp.dduha.domain.place.restaurant.repository.RestaurantImgRepository;
import com.innocamp.dduha.domain.place.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.RESTAURANT_NOT_FOUND;

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
    public ResponseEntity<?> getRestaurantList(int page, String region, String station) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Sort sort = Sort.by("likeNum").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<Restaurant> restaurants;
        List<PlaceResponseDto> restaurantResponseDtoList = new ArrayList<>();

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

        for (Restaurant restaurant : restaurants) {
            boolean hasNearbyStation = false;

            boolean isBookmarked = false;
            List<RestaurantNearby> restaurantNearbyList = restaurantNearbyRepository.findAllByRestaurant(restaurant);
            if (restaurantNearbyList.size() > 0) {
                hasNearbyStation = true;
            }
            if (null != member) {
                RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
                if (null != findRestaurantBookmark) {
                    isBookmarked = true;
                }
            }
            int bookmarkNum = restaurant.getLikeNum() + restaurantBookmarkRepository.countByRestaurant(restaurant);
            restaurantResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(restaurant.getId())
                            .name(restaurant.getName())
                            .description(restaurant.getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(restaurant.getRegion())
                            .address(restaurant.getAddress())
                            .latitude(restaurant.getLatitude())
                            .longitude(restaurant.getLongitude())
                            .thumbnailUrl(restaurant.getThumbnailUrl())
                            .hasNearStation(hasNearbyStation)
                            .isBookmarked(isBookmarked)
                            .build()
            );
        }

        ListResponseDto listResponseDto = ListResponseDto.builder()
                .totalPages(restaurants.getTotalPages())
                .nextPage(page + 1)
                .list(restaurantResponseDtoList).build();

        return ResponseEntity.ok(ResponseDto.success(listResponseDto));
    }


    public ResponseEntity<?> getRestaurantDetail(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(RESTAURANT_NOT_FOUND)));
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

        boolean isBookmarked = false;
        if (null != member) {
            RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
            if (null != findRestaurantBookmark) {
                isBookmarked = true;
            }
        }

        DetailResponseDto responseDto = DetailResponseDto.builder()
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

        return ResponseEntity.ok(ResponseDto.success(responseDto));
    }

}

