package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.response.TouristSpotResponseDto;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristSpotService {

    private final TouristSpotRepository touristSpotRepository;

    //private final TouristSpotImgRepository touristSpotImgRepository;

    public List<TouristSpotResponseDto> getTouristSpotList(HttpServletRequest request) {
        // 로그인 확인 필요
        // 즐겨찾기 때문에...

        List<TouristSpot> touristSpotList = touristSpotRepository.findAll();
        List<TouristSpotResponseDto> touristSpotResponseDtoList = new ArrayList<>();
        //String imgUrl = "";

        for (TouristSpot touristSpot : touristSpotList) {

            //imgUrl = touristSpotImgRepository.findBySpot(touristSpot).getImgUrl();

            touristSpotResponseDtoList.add(
                TouristSpotResponseDto.builder()
                    .id(touristSpot.getId())
                    .name(touristSpot.getName())
                    .description(touristSpot.getDescription())
                    .likeNum(touristSpot.getLikeNum())
                    .region(touristSpot.getRegion())
                    .thumbnailUrl(touristSpot.getThumbnailUrl())
                    //.imgUrl(imgUrl)
                    .build()
            );
        }

        return touristSpotResponseDtoList;


    }

}
