package com.innocamp.dduha.domain.place.accommodation.service;

import com.innocamp.dduha.domain.place.accommodation.model.Accommodation;
import com.innocamp.dduha.domain.place.accommodation.model.AccommodationImg;
import com.innocamp.dduha.domain.place.accommodation.model.AccommodationNearby;
import com.innocamp.dduha.domain.place.accommodation.repository.AccommodationImgRepository;
import com.innocamp.dduha.domain.place.accommodation.repository.AccommodationNearbyRepository;
import com.innocamp.dduha.domain.place.accommodation.repository.AccommodationRepository;
import com.innocamp.dduha.domain.bookmark.model.AccommodationBookmark;
import com.innocamp.dduha.domain.bookmark.repository.AccommodationBookmarkRepository;
import com.innocamp.dduha.domain.place.bustation.dto.response.BusStationResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.DetailResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.ListResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.PlaceResponseDto;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.review.dto.response.ReviewResponseDto;
import com.innocamp.dduha.domain.review.model.AccommodationReview;
import com.innocamp.dduha.domain.review.repository.AccommodationReviewRepository;
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

import static com.innocamp.dduha.global.exception.ErrorCode.ACCOMMODATION_NOT_FOUND;

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
    public ResponseEntity<?> getAccommodationList(int page, String region, String station) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Sort sort = Sort.by("likeNum").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);

        Page<Accommodation> accommodations;
        List<PlaceResponseDto> accommodationResponseDtoList = new ArrayList<>();

        if (station == null) {
            accommodations = accommodationRepository.findAll(pageable);
            if(region != null) {
                if (region.equals("우도")) {
                    accommodations = accommodationRepository.findByRegionOrRegionOrRegion(pageable, "성산", "우도", "표선");
                } else if (region.equals("구좌")) {
                    accommodations = accommodationRepository.findByRegionOrRegionOrRegion(pageable, "구좌", "조천", "비어있음");
                } else if (region.equals("애월")) {
                    accommodations = accommodationRepository.findByRegionOrRegionOrRegion(pageable, "애월", "한림", "비어있음");
                } else {
                    accommodations = accommodationRepository.findByRegion(pageable, region);
                }
            }
        } else {
            accommodations = accommodationRepository.findByHasStation(pageable);
            if(region != null) {
                if (region.equals("우도")) {
                    accommodations = accommodationRepository.findByHasStationAndRegion(pageable, "성산", "우도", "표선");
                } else if (region.equals("구좌")) {
                    accommodations = accommodationRepository.findByHasStationAndRegion(pageable, "구좌", "조천", "비어있음");
                } else if (region.equals("애월")) {
                    accommodations = accommodationRepository.findByHasStationAndRegion(pageable, "애월", "한림", "비어있음");
                } else {
                    accommodations = accommodationRepository.findByHasStationAndRegion(pageable, region, "비어있음", "비어있음");
                }
            }
        }

        for (Accommodation accommodation : accommodations) {
            boolean hasNearbyStation = false;
            boolean isBookmarked = false;
            List<AccommodationNearby> accommodationNearbyList = accommodationNearbyRepository
                    .findAllByAccommodation(accommodation);
            if (accommodationNearbyList.size() > 0) {
                hasNearbyStation = true;
            }
            if (member != null) {
                AccommodationBookmark accommodationBookmark = accommodationBookmarkRepository
                        .findByMemberAndAccommodation(member, accommodation);
                if (null != accommodationBookmark) {
                    isBookmarked = true;
                }
            }
            int bookmarkNum = accommodation.getLikeNum() + accommodationBookmarkRepository.countByAccommodation(accommodation);
            accommodationResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(accommodation.getId())
                            .name(accommodation.getName())
                            .description(accommodation.getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(accommodation.getRegion())
                            .address(accommodation.getAddress())
                            .latitude(accommodation.getLatitude())
                            .longitude(accommodation.getLongitude())
                            .thumbnailUrl(accommodation.getThumbnailUrl())
                            .hasNearStation(hasNearbyStation)
                            .isBookmarked(isBookmarked)
                            .build()
            );
        }

        ListResponseDto listResponseDto = ListResponseDto.builder()
                .totalPages(accommodations.getTotalPages())
                .nextPage(page+1)
                .list(accommodationResponseDtoList).build();

        return ResponseEntity.ok(ResponseDto.success(listResponseDto));
    }


    public ResponseEntity<?> getAccommodationDetail(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(ACCOMMODATION_NOT_FOUND)));
        List<AccommodationImg> accommodationImgList = accommodationImgRepository.findAllByAccommodation(accommodation);
        List<String> accommodationImgs = new ArrayList<>();
        for (AccommodationImg accommodationImg : accommodationImgList) {
            accommodationImgs.add(accommodationImg.getImgUrl());
        }

        List<AccommodationNearby> accommodationNearbyList = accommodationNearbyRepository.findAllByAccommodation(accommodation);
        List<BusStationResponseDto> busStationResponseDtoList = new ArrayList<>();
        for (AccommodationNearby accommodationNearby : accommodationNearbyList) {
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

        int bookmarkNum = accommodation.getLikeNum() + accommodationBookmarkRepository.countByAccommodation(accommodation);

        boolean isBookmarked = false;
        if (null != member) {
            AccommodationBookmark findAccommodationBookmark = accommodationBookmarkRepository.findByMemberAndAccommodation(member, accommodation);
            if (null != findAccommodationBookmark) {
                isBookmarked = true;
            }
        }

        DetailResponseDto responseDto = DetailResponseDto.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .description(accommodation.getDescription())
                .address(accommodation.getAddress())
                .phone(accommodation.getPhone())
                .info(accommodation.getInfo())
                .bookmarkNum(bookmarkNum)
                .thumbnailUrl(accommodation.getThumbnailUrl())
                .region(accommodation.getRegion())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .imgUrl(accommodationImgs)
                .reviews(reviewResponseDtoList)
                .stations(busStationResponseDtoList)
                .isBookmarked(isBookmarked)
                .build();

        return ResponseEntity.ok(ResponseDto.success(responseDto));
    }

}
