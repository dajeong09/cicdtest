package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.BusStationResponseDto;
import com.innocamp.dduha.dto.response.DetailResponseDto;
import com.innocamp.dduha.dto.response.ReviewResponseDto;
import com.innocamp.dduha.dto.response.TouristSpotResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.nearby.TouristSpotNearby;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.model.touristspot.TouristSpotImg;
import com.innocamp.dduha.model.review.TouristSpotReview;
import com.innocamp.dduha.repository.bookmark.TouristSpotBookmarkRepository;
import com.innocamp.dduha.repository.nearby.TouristSpotNearbyRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotImgRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import com.innocamp.dduha.repository.review.TouristSpotReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TouristSpotService {

    private final TouristSpotRepository touristSpotRepository;
    private final TouristSpotImgRepository touristSpotImgRepository;
    private final TouristSpotReviewRepository touristSpotReviewRepository;
    private final TokenProvider tokenProvider;

    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final TouristSpotNearbyRepository touristSpotNearbyRepository;

    public ResponseDto<?> getTouristSpotList() {

        // 유효한 사용자라는 가정 하에 로그인 상태 확인 (추후 수정) __ 로그인이 안 되어 있으면 null
        Member member = tokenProvider.getMemberFromAuthentication();

        List<TouristSpot> touristSpotList = touristSpotRepository.findAll();
        List<TouristSpotResponseDto> touristSpotResponseDtoList = new ArrayList<>();

        if (null != member) {
            for (TouristSpot touristSpot : touristSpotList) {
                boolean hasNearbyStation = false;
                boolean isBookmarked = false;
                List<TouristSpotNearby> touristSpotNearbyList = touristSpotNearbyRepository.findAllByTouristSpot(touristSpot);
                if (touristSpotNearbyList.size() > 0) {
                    hasNearbyStation = true;
                }
                TouristSpotBookmark findTouristSpotBookmark = touristSpotBookmarkRepository.findByMemberAndTouristSpot(member, touristSpot);
                if (null != findTouristSpotBookmark) {
                    isBookmarked = true;
                }

                touristSpotResponseDtoList.add(
                        TouristSpotResponseDto.builder()
                                .id(touristSpot.getId())
                                .name(touristSpot.getName())
                                .description(touristSpot.getDescription())
                                .likeNum(touristSpot.getLikeNum())
                                .region(touristSpot.getRegion())
                                .thumbnailUrl(touristSpot.getThumbnailUrl())
                                .hasNearStation(hasNearbyStation)
                                .isBookmarked(isBookmarked)
                                .build()
                );
            }
        } else {
            for (TouristSpot touristSpot : touristSpotList) {
                boolean hasNearbyStation = false;
                List<TouristSpotNearby> touristSpotNearbyList = touristSpotNearbyRepository.findAllByTouristSpot(touristSpot);
                if (touristSpotNearbyList.size() > 0) {
                    hasNearbyStation = true;
                }
                touristSpotResponseDtoList.add(
                        TouristSpotResponseDto.builder()
                                .id(touristSpot.getId())
                                .name(touristSpot.getName())
                                .description(touristSpot.getDescription())
                                .likeNum(touristSpot.getLikeNum())
                                .region(touristSpot.getRegion())
                                .thumbnailUrl(touristSpot.getThumbnailUrl())
                                .hasNearStation(hasNearbyStation)
                                .build()
                );
            }
        }
        return ResponseDto.success(touristSpotResponseDtoList);
    }

    public ResponseDto<?> getTouristSpotDetail(Long id) {
        // 유효한 사용자라는 가정 하에 로그인 상태 확인 (추후 수정) __ 로그인이 안 되어 있으면 null
        Member member = tokenProvider.getMemberFromAuthentication();

        TouristSpot touristSpot = isPresentTouristSpot(id);
        List<TouristSpotImg> touristSpotImgList = touristSpotImgRepository.findAllByTouristSpot(touristSpot);
        List<String> touristSpotImgs = new ArrayList<>();
        for (TouristSpotImg touristSpotImg : touristSpotImgList) {
            touristSpotImgs.add(touristSpotImg.getImgUrl());
        }

        List<TouristSpotNearby> touristSpotNearbyList = touristSpotNearbyRepository.findAllByTouristSpot(touristSpot);
        List<BusStationResponseDto> busStationResponseDtoList = new ArrayList<>();
        for(TouristSpotNearby touristSpotNearby : touristSpotNearbyList) {
            busStationResponseDtoList.add(
                    BusStationResponseDto.builder()
                            .stationName(touristSpotNearby.getBusStation().getStationName())
                            .distance(touristSpotNearby.getDistance()).build()
            );
        }

        List<TouristSpotReview> touristSpotReviewList = touristSpotReviewRepository.findAllByTouristSpotOrderByCreatedAtDesc(touristSpot);
        List<ReviewResponseDto> touristSpotReviewResponseDtoList = new ArrayList<>();
        for (TouristSpotReview touristSpotReview : touristSpotReviewList) {
            touristSpotReviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .id(touristSpotReview.getId())
                            .reviewer(touristSpotReview.getMember().getNickname())
                            .review(touristSpotReview.getReview())
                            .reviewedAt(touristSpotReview.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).build()
            );
        }

        DetailResponseDto responseDto;
        if (null != member) {
            boolean isBookmarked = false;
            TouristSpotBookmark findTouristSpotBookmark = touristSpotBookmarkRepository.findByMemberAndTouristSpot(member, touristSpot);
            if (null != findTouristSpotBookmark) {
                isBookmarked = true;
            }
            responseDto = DetailResponseDto.builder()
                    .id(touristSpot.getId())
                    .name(touristSpot.getName())
                    .description(touristSpot.getDescription())
                    .address(touristSpot.getAddress())
                    .phone(touristSpot.getPhone())
                    .info(touristSpot.getInfo())
                    .likeNum(touristSpot.getLikeNum())
                    .thumbnailUrl(touristSpot.getThumbnailUrl())
                    .region(touristSpot.getRegion())
                    .latitude(touristSpot.getLatitude())
                    .longitude(touristSpot.getLongitude())
                    .stations(busStationResponseDtoList)
                    .imgUrl(touristSpotImgs)
                    .reviews(touristSpotReviewResponseDtoList)
                    .isBookmarked(isBookmarked)
                    .build();

        } else {
            responseDto = DetailResponseDto.builder()
                    .id(touristSpot.getId())
                    .name(touristSpot.getName())
                    .description(touristSpot.getDescription())
                    .address(touristSpot.getAddress())
                    .phone(touristSpot.getPhone())
                    .info(touristSpot.getInfo())
                    .likeNum(touristSpot.getLikeNum())
                    .thumbnailUrl(touristSpot.getThumbnailUrl())
                    .region(touristSpot.getRegion())
                    .latitude(touristSpot.getLatitude())
                    .longitude(touristSpot.getLongitude())
                    .stations(busStationResponseDtoList)
                    .imgUrl(touristSpotImgs)
                    .reviews(touristSpotReviewResponseDtoList)
                    .build();
        }
        return ResponseDto.success(responseDto);
    }

    @Transactional
    public TouristSpot isPresentTouristSpot(Long id) {
        Optional<TouristSpot> optionalTouristSpot = touristSpotRepository.findTouristSpotById(id);
        return optionalTouristSpot.orElse(null);
    }

}
