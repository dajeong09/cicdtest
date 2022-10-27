package com.innocamp.dduha.domain.mypage.service;

import com.innocamp.dduha.domain.mypage.dto.response.MyPageDetailResponseDto;
import com.innocamp.dduha.domain.mypage.dto.response.MyPageResponseDto;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.bookmark.model.AccommodationBookmark;
import com.innocamp.dduha.domain.bookmark.model.RestaurantBookmark;
import com.innocamp.dduha.domain.bookmark.model.TouristSpotBookmark;
import com.innocamp.dduha.domain.bookmark.model.TripBookmark;
import com.innocamp.dduha.domain.review.dto.response.ReviewResponseDto;
import com.innocamp.dduha.domain.review.model.AccommodationReview;
import com.innocamp.dduha.domain.review.model.RestaurantReview;
import com.innocamp.dduha.domain.review.model.TouristSpotReview;
import com.innocamp.dduha.domain.bookmark.repository.AccommodationBookmarkRepository;
import com.innocamp.dduha.domain.bookmark.repository.RestaurantBookmarkRepository;
import com.innocamp.dduha.domain.bookmark.repository.TouristSpotBookmarkRepository;
import com.innocamp.dduha.domain.bookmark.repository.TripBookmarkRepository;
import com.innocamp.dduha.domain.review.repository.AccommodationReviewRepository;
import com.innocamp.dduha.domain.review.repository.RestaurantReviewRepository;
import com.innocamp.dduha.domain.review.repository.TouristSpotReviewRepository;
import com.innocamp.dduha.domain.trip.dto.response.TripResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TripBookmarkRepository tripBookmarkRepository;
    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final AccommodationBookmarkRepository accommodationBookmarkRepository;
    private final TokenProvider tokenProvider;
    private final TouristSpotReviewRepository touristSpotReviewRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;
    private final AccommodationReviewRepository accommodationReviewRepository;

    // 내가 즐겨찾기한 각 목록 조회(즐겨찾기 개수)
    public ResponseEntity<?> getMyBookmarkedList() {

        Member member = tokenProvider.getMemberFromAuthentication();

        int tripBookmarkNum = tripBookmarkRepository.countTripBookmarkByMember(member);
        int touristSpotBookmarkNum = touristSpotBookmarkRepository.countTouristSpotBookmarkByMember(member);
        int restaurantBookmarkNum = restaurantBookmarkRepository.countRestaurantBookmarkByMember(member);
        int accommodationBookmarkNum = accommodationBookmarkRepository.countAccommodationBookmarkByMember(member);

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .tripBookmarkNum(tripBookmarkNum)
                .touristSpotBookmarkNum(touristSpotBookmarkNum)
                .restaurantBookmarkNum(restaurantBookmarkNum)
                .accommodationBookmarkNum(accommodationBookmarkNum)
                .build();
        return ResponseEntity.ok(ResponseDto.success(myPageResponseDto));
    }

    // 내가 즐겨찾기한 관광지 조회
    public ResponseEntity<?> getMyTouristSpotBookmark() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<TouristSpotBookmark> touristSpotBookmarkList = touristSpotBookmarkRepository.findAllByMember(member);
        List<MyPageDetailResponseDto> touristSpotResponseDtoList = new ArrayList<>();

        for (TouristSpotBookmark touristSpotBookmark : touristSpotBookmarkList) {
            int bookmarkNum = touristSpotBookmark.getTouristSpot().getLikeNum() + touristSpotBookmarkRepository.countByTouristSpot(touristSpotBookmark.getTouristSpot());
            touristSpotResponseDtoList.add(
                    MyPageDetailResponseDto.builder()
                            .id(touristSpotBookmark.getTouristSpot().getId())
                            .name(touristSpotBookmark.getTouristSpot().getName())
                            .description(touristSpotBookmark.getTouristSpot().getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(touristSpotBookmark.getTouristSpot().getRegion())
                            .thumbnailUrl(touristSpotBookmark.getTouristSpot().getThumbnailUrl())
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseEntity.ok(ResponseDto.success(touristSpotResponseDtoList));
    }


    //내가 즐겨찾기한 맛집 조회
    public ResponseEntity<?> getMyRestaurantBookmark() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<RestaurantBookmark> restaurantBookmarkList = restaurantBookmarkRepository.findAllByMember(member);
        List<MyPageDetailResponseDto> RestaurantResponseDtoList = new ArrayList<>();

        for (RestaurantBookmark restaurantBookmark : restaurantBookmarkList) {
            int bookmarkNum = restaurantBookmark.getRestaurant().getLikeNum() + restaurantBookmarkRepository.countByRestaurant(restaurantBookmark.getRestaurant());
            RestaurantResponseDtoList.add(
                    MyPageDetailResponseDto.builder()
                            .id(restaurantBookmark.getRestaurant().getId())
                            .name(restaurantBookmark.getRestaurant().getName())
                            .description(restaurantBookmark.getRestaurant().getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(restaurantBookmark.getRestaurant().getRegion())
                            .thumbnailUrl(restaurantBookmark.getRestaurant().getThumbnailUrl())
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseEntity.ok(ResponseDto.success(RestaurantResponseDtoList));
    }

    //내가 즐겨찾기한 숙소 조회
    public ResponseEntity<?> getMyAccommodationBookmark() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<AccommodationBookmark> accommodationBoookmarkList = accommodationBookmarkRepository.findAllByMember(member);
        List<MyPageDetailResponseDto> AccommodationResponseDtoList = new ArrayList<>();

        for (AccommodationBookmark accommodationBookmark : accommodationBoookmarkList) {
            int bookmarkNum = accommodationBookmark.getAccommodation().getLikeNum() + accommodationBookmarkRepository.countByAccommodation(accommodationBookmark.getAccommodation());
            AccommodationResponseDtoList.add(
                    MyPageDetailResponseDto.builder()
                            .id(accommodationBookmark.getAccommodation().getId())
                            .name(accommodationBookmark.getAccommodation().getName())
                            .description(accommodationBookmark.getAccommodation().getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(accommodationBookmark.getAccommodation().getRegion())
                            .thumbnailUrl(accommodationBookmark.getAccommodation().getThumbnailUrl())
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseEntity.ok(ResponseDto.success(AccommodationResponseDtoList));
    }

    //내가 즐겨찾기한 일정 조회
    public ResponseEntity<?> getMyTripBookmark() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<TripBookmark> tripBookmarkList = tripBookmarkRepository.findAllByMember(member);
        List<TripResponseDto> TripResponseDtoList = new ArrayList<>();

        for (TripBookmark tripBookmark : tripBookmarkList) {
            TripResponseDtoList.add(
                    TripResponseDto.builder()
                            .id(tripBookmark.getTrip().getId())
                            .title(tripBookmark.getTrip().getTitle())
                            .startAt(tripBookmark.getTrip().getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                            .endAt(tripBookmark.getTrip().getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseEntity.ok(ResponseDto.success(TripResponseDtoList));
    }

    // 내가 작성한 관광지 댓글 조회
    public ResponseEntity<?> getMyTouristSpotReview() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<TouristSpotReview> touristSpotReviewList = touristSpotReviewRepository.findAllByMember(member);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (TouristSpotReview touristSpotReview : touristSpotReviewList) {
            reviewResponseDtoList.add(ReviewResponseDto.builder()
                    .id(touristSpotReview.getId())
                    .itemId(touristSpotReview.getTouristSpot().getId())
                    .reviewName(touristSpotReview.getTouristSpot().getName())
                    .review(touristSpotReview.getReview())
                    .reviewer(touristSpotReview.getMember().getNickname())
                    .reviewedAt(touristSpotReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .build());
        }
        return ResponseEntity.ok(ResponseDto.success(reviewResponseDtoList));
    }

    // 내가 작성한 맛집 댓글 조회
    public ResponseEntity<?> getMyRestaurantReview() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<RestaurantReview> restaurantReviewList = restaurantReviewRepository.findAllByMember(member);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (RestaurantReview restaurantReview : restaurantReviewList) {
            reviewResponseDtoList.add(ReviewResponseDto.builder()
                    .id(restaurantReview.getId())
                    .itemId(restaurantReview.getRestaurant().getId())
                    .reviewName(restaurantReview.getRestaurant().getName())
                    .review(restaurantReview.getReview())
                    .reviewer(restaurantReview.getMember().getNickname())
                    .reviewedAt(restaurantReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .build());
        }
        return ResponseEntity.ok(ResponseDto.success(reviewResponseDtoList));
    }

    // 내가 작성한 숙소 댓글 조회
    public ResponseEntity<?> getMyAccommodationReview() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<AccommodationReview> accommodationReviewList = accommodationReviewRepository.findAllByMember(member);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (AccommodationReview accommodationReview : accommodationReviewList) {
            reviewResponseDtoList.add(ReviewResponseDto.builder()
                    .id(accommodationReview.getId())
                    .itemId(accommodationReview.getAccommodation().getId())
                    .reviewName(accommodationReview.getAccommodation().getName())
                    .review(accommodationReview.getReview())
                    .reviewer(accommodationReview.getMember().getNickname())
                    .reviewedAt(accommodationReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .build());
        }
        return ResponseEntity.ok(ResponseDto.success(reviewResponseDtoList));
    }
}
