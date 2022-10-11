package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.AccommodationResponseDto;
import com.innocamp.dduha.dto.response.BusStationResponseDto;
import com.innocamp.dduha.dto.response.DetailResponseDto;
import com.innocamp.dduha.dto.response.ReviewResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.accommodation.AccommodationImg;
import com.innocamp.dduha.model.accommodation.AccommodationReview;
import com.innocamp.dduha.model.bookmark.AccommodationBookmark;
import com.innocamp.dduha.model.nearby.AccommodationNearby;
import com.innocamp.dduha.repository.accommodation.AccommodationImgRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationReviewRepository;
import com.innocamp.dduha.repository.bookmark.AccommodationBookmarkRepository;
import com.innocamp.dduha.repository.nearby.AccommodationNearbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationReviewRepository accommodationReviewRepository;
    private final AccommodationImgRepository accommodationImgRepository;
    private final AccommodationBookmarkRepository accommodationBookmarkRepository;
    private final AccommodationNearbyRepository accommodationNearbyRepository;

    private final TokenProvider tokenProvider;

    public ResponseDto<?> getAccommodationList() {

        // 사용자 검증 추가 필요
        Member member = tokenProvider.getMemberFromAuthentication();

        List<Accommodation> accommodationList = accommodationRepository.findAll();
        List<AccommodationResponseDto> accommodationResponseDtoList = new ArrayList<>();

        if (null != member) {
            for (Accommodation accommodation : accommodationList) {
                boolean hasNearbyStation = false;
                boolean isBookmarked = false;
                List<AccommodationNearby> accommodationNearbyList = accommodationNearbyRepository.findAllByAccommodation(accommodation);
                if (accommodationNearbyList.size() > 0) {
                    hasNearbyStation = true;
                }
                AccommodationBookmark accommodationBookmark = accommodationBookmarkRepository.findByMemberAndAccommodation(member, accommodation);
                if (null != accommodationBookmark) {
                    isBookmarked = true;
                }

                accommodationResponseDtoList.add(
                        AccommodationResponseDto.builder()
                                .id(accommodation.getId())
                                .name(accommodation.getName())
                                .description(accommodation.getDescription())
                                .likeNum(accommodation.getLikeNum())
                                .region(accommodation.getRegion())
                                .thumbnailUrl(accommodation.getThumbnailUrl())
                                .hasNearStation(hasNearbyStation)
                                .isBookmarked(isBookmarked)
                                .build()
                );
            }
        } else {
            for (Accommodation accommodation : accommodationList) {
                boolean hasNearbyStation = false;
                List<AccommodationNearby> accommodationNearbyList = accommodationNearbyRepository.findAllByAccommodation(accommodation);
                if (accommodationNearbyList.size() > 0) {
                    hasNearbyStation = true;
                }
                accommodationResponseDtoList.add(
                        AccommodationResponseDto.builder()
                                .id(accommodation.getId())
                                .name(accommodation.getName())
                                .description(accommodation.getDescription())
                                .likeNum(accommodation.getLikeNum())
                                .region(accommodation.getRegion())
                                .thumbnailUrl(accommodation.getThumbnailUrl())
                                .hasNearStation(hasNearbyStation)
                                .build()
                );
            }
        }
        return ResponseDto.success(accommodationResponseDtoList);

    }


    public ResponseDto<?> getAccommodationDetail(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Accommodation accommodation = isPresentAccommodation(id);
        List<AccommodationImg> accommodationImgList = accommodationImgRepository.findAllByAccommodation(accommodation);
        List<String> accommodationImgs = new ArrayList<>();
        for (AccommodationImg accommodationImg : accommodationImgList) {
            accommodationImgs.add(accommodationImg.getImgUrl());
        }

        List<AccommodationNearby> accommodationNearbyList = accommodationNearbyRepository.findAllByAccommodation(accommodation);
        List<BusStationResponseDto> busStationResponseDtoList = new ArrayList<>();
        for(AccommodationNearby accommodationNearby : accommodationNearbyList) {
            busStationResponseDtoList.add(
                    BusStationResponseDto.builder()
                            .stationName(accommodationNearby.getBusStation().getStationName())
                            .distance(accommodationNearby.getDistance()).build()
            );
        }

        List<AccommodationReview> accommodationReviewList = accommodationReviewRepository.findAllByAccommodationOrderByReviewedAtDesc(accommodation);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (AccommodationReview accommodationReview : accommodationReviewList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .id(accommodationReview.getId())
                            .reviewer(accommodationReview.getReviewer())
                            .review(accommodationReview.getReview()).build()
            );
        }

        DetailResponseDto responseDto;
        if (null != member) {
            boolean isBookmarked = false;
            AccommodationBookmark findAccommodationBookmark = accommodationBookmarkRepository.findByMemberAndAccommodation(member, accommodation);
            if (null != findAccommodationBookmark) {
                isBookmarked = true;
            }
            responseDto = DetailResponseDto.builder()
                    .id(accommodation.getId())
                    .name(accommodation.getName())
                    .description(accommodation.getDescription())
                    .address(accommodation.getAddress())
                    .phone(accommodation.getPhone())
                    .info(accommodation.getInfo())
                    .likeNum(accommodation.getLikeNum())
                    .thumbnailUrl(accommodation.getThumbnailUrl())
                    .region(accommodation.getRegion())
                    .latitude(accommodation.getLatitude())
                    .longitude(accommodation.getLongitude())
                    .imgUrl(accommodationImgs)
                    .reviews(reviewResponseDtoList)
                    .stations(busStationResponseDtoList)
                    .isBookmarked(isBookmarked)
                    .build();

        } else {
            responseDto = DetailResponseDto.builder()
                    .id(accommodation.getId())
                    .name(accommodation.getName())
                    .description(accommodation.getDescription())
                    .address(accommodation.getAddress())
                    .phone(accommodation.getPhone())
                    .info(accommodation.getInfo())
                    .likeNum(accommodation.getLikeNum())
                    .thumbnailUrl(accommodation.getThumbnailUrl())
                    .region(accommodation.getRegion())
                    .latitude(accommodation.getLatitude())
                    .longitude(accommodation.getLongitude())
                    .imgUrl(accommodationImgs)
                    .reviews(reviewResponseDtoList)
                    .stations(busStationResponseDtoList)
                    .build();
        }
        return ResponseDto.success(responseDto);
    }

    @Transactional
    public Accommodation isPresentAccommodation(Long id) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(id);
        return optionalAccommodation.orElse(null);
    }


}
