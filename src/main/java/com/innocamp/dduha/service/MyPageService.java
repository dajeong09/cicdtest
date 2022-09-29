package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.*;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.AccommodationBookmark;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.bookmark.TripBookmark;
import com.innocamp.dduha.repository.bookmark.AccommodationBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.TouristSpotBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.TripBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TripBookmarkRepository tripBookmarkRepository;
    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final AccommodationBookmarkRepository accommodationBookmarkRepository;

    private final MemberService memberService;

    // 내가 즐겨찾기한 각 목록 조회(즐겨찾기 개수)
    public ResponseDto<?> getMyBookmarkedList(HttpServletRequest request) {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

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
        return ResponseDto.success(myPageResponseDto);
    }

    // 내가 즐겨찾기한 관광지 조회
    public ResponseDto<?> getMyTouristSpotBookmark(HttpServletRequest request) {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        List<TouristSpotBookmark> touristSpotBookmarkList = touristSpotBookmarkRepository.findAllByMember(member);
        List<TouristSpotResponseDto> touristSpotResponseDtoList = new ArrayList<>();

        for (TouristSpotBookmark touristSpotBookmark : touristSpotBookmarkList) {
            touristSpotResponseDtoList.add(
                    TouristSpotResponseDto.builder()
                            .id(touristSpotBookmark.getTouristSpot().getId())
                            .name(touristSpotBookmark.getTouristSpot().getName())
                            .description(touristSpotBookmark.getTouristSpot().getDescription())
                            .likeNum(touristSpotBookmark.getTouristSpot().getLikeNum())
                            .region(touristSpotBookmark.getTouristSpot().getRegion())
                            .thumbnailUrl(touristSpotBookmark.getTouristSpot().getThumbnailUrl())
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseDto.success(touristSpotResponseDtoList);
    }


    //내가 즐겨찾기한 맛집 조회
    public ResponseDto<?> getMyRestaurantBookmark(HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }
        List<RestaurantBookmark> restaurantBookmarkList = restaurantBookmarkRepository.findAllByMember(member);
        List<RestaurantResponseDto> RestaurantResponseDtoList = new ArrayList<>();

        for (RestaurantBookmark restaurantBookmark : restaurantBookmarkList) {
            RestaurantResponseDtoList.add(
                    RestaurantResponseDto.builder()
                            .id(restaurantBookmark.getRestaurant().getId())
                            .name(restaurantBookmark.getRestaurant().getName())
                            .description(restaurantBookmark.getRestaurant().getDescription())
                            .likeNum(restaurantBookmark.getRestaurant().getLikeNum())
                            .region(restaurantBookmark.getRestaurant().getRegion())
                            .thumbnailUrl(restaurantBookmark.getRestaurant().getThumbnailUrl())
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseDto.success(RestaurantResponseDtoList);
    }

    //내가 즐겨찾기한 숙소 조회
    public ResponseDto<?> getMyAccommodationBookmark(HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }
        List<AccommodationBookmark> accommodationBoookmarkList = accommodationBookmarkRepository.findAllByMember(member);
        List<AccommodationResponseDto> AccommodationResponseDtoList = new ArrayList<>();

        for (AccommodationBookmark accommodationBookmark : accommodationBoookmarkList) {
            AccommodationResponseDtoList.add(
                    AccommodationResponseDto.builder()
                            .id(accommodationBookmark.getAccommodation().getId())
                            .name(accommodationBookmark.getAccommodation().getName())
                            .description(accommodationBookmark.getAccommodation().getDescription())
                            .likeNum(accommodationBookmark.getAccommodation().getLikeNum())
                            .region(accommodationBookmark.getAccommodation().getRegion())
                            .thumbnailUrl(accommodationBookmark.getAccommodation().getThumbnailUrl())
                            .isBookmarked(true)
                            .build()
            );
        }
        return ResponseDto.success(AccommodationResponseDtoList);
    }

    //내가 즐겨찾기한 일정 조회
    public ResponseDto<?> getMyTripBookmark(HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }
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
        return ResponseDto.success(TripResponseDtoList);
    }
}
