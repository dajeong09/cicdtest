package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.AccommodationResponseDto;
import com.innocamp.dduha.dto.response.DetailResponseDto;
import com.innocamp.dduha.dto.response.ReviewResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.accommodation.AccommodationImg;
import com.innocamp.dduha.model.accommodation.AccommodationReview;
import com.innocamp.dduha.repository.accommodation.AccommodationImgRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationReviewRepository;
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

    private final TokenProvider tokenProvider;

    public ResponseDto<?> getAccommodationList() {

        // 사용자 검증 추가 필요

        List<Accommodation> accommodationList = accommodationRepository.findAll();
        List<AccommodationResponseDto> accommodationResponseDtoList = new ArrayList<>();

        for (Accommodation accommodation : accommodationList) {

            accommodationResponseDtoList.add(
                    AccommodationResponseDto.builder()
                            .id(accommodation.getId())
                            .name(accommodation.getName())
                            .description(accommodation.getDescription())
                            .likeNum(accommodation.getLikeNum())
                            .region(accommodation.getRegion())
                            .thumbnailUrl(accommodation.getThumbnailUrl())
                            .build()
            );
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
//            RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
//            if (null != findRestaurantBookmark) {
//                isBookmarked = true;
//            }
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
                    .imgUrl(accommodationImgs)
                    .reviews(reviewResponseDtoList)
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
                    .imgUrl(accommodationImgs)
                    .reviews(reviewResponseDtoList)
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
