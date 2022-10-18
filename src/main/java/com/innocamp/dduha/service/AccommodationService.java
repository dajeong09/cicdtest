package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.*;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.accommodation.AccommodationImg;
import com.innocamp.dduha.model.review.AccommodationReview;
import com.innocamp.dduha.model.bookmark.AccommodationBookmark;
import com.innocamp.dduha.model.nearby.AccommodationNearby;
import com.innocamp.dduha.repository.accommodation.AccommodationImgRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.review.AccommodationReviewRepository;
import com.innocamp.dduha.repository.bookmark.AccommodationBookmarkRepository;
import com.innocamp.dduha.repository.nearby.AccommodationNearbyRepository;
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
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationReviewRepository accommodationReviewRepository;
    private final AccommodationImgRepository accommodationImgRepository;
    private final AccommodationBookmarkRepository accommodationBookmarkRepository;
    private final AccommodationNearbyRepository accommodationNearbyRepository;

    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public ResponseDto<?> getAccommodationList(int page, String region) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Sort sort = Sort.by("likeNum").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Accommodation> accommodations;

        if(region == null) {
            accommodations = accommodationRepository.findAll(pageable);
        } else {
            if (region.equals("성산") || region.equals("우도")) {
                accommodations = accommodationRepository.findByRegionOrRegion(pageable, "성산", "우도");
            } else if (region.equals("구좌") || region.equals("조천")) {
                accommodations = accommodationRepository.findByRegionOrRegion(pageable, "구좌", "조천");
            } else {
                accommodations = accommodationRepository.findByRegion(pageable, region);
            }
        }

        List<AccommodationResponseDto> accommodationResponseDtoList = new ArrayList<>();

        if (null != member) {
            for (Accommodation accommodation : accommodations) {
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
            for (Accommodation accommodation : accommodations) {
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


        ListResponseDto listResponseDto = ListResponseDto.builder()
                .totalPages(accommodations.getTotalPages())
                .list(accommodationResponseDtoList).build();

        return ResponseDto.success(listResponseDto);
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

        List<AccommodationReview> accommodationReviewList = accommodationReviewRepository.findAllByAccommodationOrderByCreatedAtDesc(accommodation);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        for (AccommodationReview accommodationReview : accommodationReviewList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .id(accommodationReview.getId())
                            .reviewer(accommodationReview.getMember().getNickname())
                            .review(accommodationReview.getReview())
                            .reviewedAt(accommodationReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).build()
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
