package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.AccommodationResponseDto;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

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



}
