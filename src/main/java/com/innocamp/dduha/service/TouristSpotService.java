package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.TouristSpotDetailResponseDto;
import com.innocamp.dduha.dto.response.TouristSpotResponseDto;
import com.innocamp.dduha.dto.response.TouristSpotReviewResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.model.touristspot.TouristSpotImg;
import com.innocamp.dduha.model.touristspot.TouristSpotReview;
import com.innocamp.dduha.repository.touristspot.TouristSpotImgRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TouristSpotService {

    private final TouristSpotRepository touristSpotRepository;
    private final TouristSpotImgRepository touristSpotImgRepository;
    private final TouristSpotReviewRepository touristSpotReviewRepository;
    private final TokenProvider tokenProvider;

    public ResponseDto<?> getTouristSpotList(HttpServletRequest request) {

        // 유효한 사용자라는 가정 하에 로그인 상태 확인 (추후 수정) __ 로그인이 안 되어 있으면 null
        Member member = tokenProvider.getMemberFromAuthentication();

        List<TouristSpot> touristSpotList = touristSpotRepository.findAll();
        List<TouristSpotResponseDto> touristSpotResponseDtoList = new ArrayList<>();

        for (TouristSpot touristSpot : touristSpotList) {

            touristSpotResponseDtoList.add(
                TouristSpotResponseDto.builder()
                    .id(touristSpot.getId())
                    .name(touristSpot.getName())
                    .description(touristSpot.getDescription())
                    .likeNum(touristSpot.getLikeNum())
                    .region(touristSpot.getRegion())
                    .thumbnailUrl(touristSpot.getThumbnailUrl())
                    .build()
            );
        }

        return ResponseDto.success(touristSpotResponseDtoList);

    }


    public ResponseDto<?> getTouristSpotDetail(Long id, HttpServletRequest request) {
        // 유효한 사용자라는 가정 하에 로그인 상태 확인 (추후 수정) __ 로그인이 안 되어 있으면 null
        Member member = tokenProvider.getMemberFromAuthentication();

        TouristSpot touristSpot = touristSpotRepository.findTouristSpotById(id);
        List<TouristSpotImg> touristSpotImgList = touristSpotImgRepository.findAllByTouristSpot(touristSpot);
        List<String> touristSpotImgs = new ArrayList<>();
        for(TouristSpotImg touristSpotImg : touristSpotImgList) {
            touristSpotImgs.add(touristSpotImg.getImgUrl());
        }
        List<TouristSpotReview> touristSpotReviewList = touristSpotReviewRepository.findAllByTouristSpotOrderByReviewedAtDesc(touristSpot);
        List<TouristSpotReviewResponseDto> touristSpotReviewResponseDtoList = new ArrayList<>();
        for(TouristSpotReview touristSpotReview : touristSpotReviewList) {
            touristSpotReviewResponseDtoList.add(
                    TouristSpotReviewResponseDto.builder()
                            .id(touristSpotReview.getId())
                            .reviewer(touristSpotReview.getReviewer())
                            .review(touristSpotReview.getReview()).build()
            );
        }

        TouristSpotDetailResponseDto responseDto = TouristSpotDetailResponseDto.builder()
                .id(touristSpot.getId())
                .name(touristSpot.getName())
                .description(touristSpot.getDescription())
                .address(touristSpot.getAddress())
                .phone(touristSpot.getPhone())
                .info(touristSpot.getInfo())
                .likeNum(touristSpot.getLikeNum())
                .thumbnailUrl(touristSpot.getThumbnailUrl())
                .region(touristSpot.getRegion())
                .imgUrl(touristSpotImgs)
                .touristSpotReviews(touristSpotReviewResponseDtoList).build();

        return ResponseDto.success(responseDto);
    }

}
