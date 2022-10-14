package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.InfoResponseDto;
import com.innocamp.dduha.dto.response.UsefulInfoResponseDto;
import com.innocamp.dduha.model.UsefulInfo;
import com.innocamp.dduha.repository.UsefulInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UsefulInfoService {

    private final UsefulInfoRepository usefulInfoRepository;

    public ResponseDto<?> getUsefulInfo() {
        List<UsefulInfo> usefulInfoList = usefulInfoRepository.findAll();

        List<UsefulInfoResponseDto> usefulInfoResponseDtoList = new ArrayList<>();
        List<InfoResponseDto> publicInfoResponseDtoList = new ArrayList<>();
        List<InfoResponseDto> bagInfoResponseDtoList = new ArrayList<>();
        List<InfoResponseDto> taxiInfoResponseDtoList = new ArrayList<>();

        for (UsefulInfo usefulInfo : usefulInfoList) {
            switch (usefulInfo.getCategory()) {
                case "대중교통":
                    publicInfoResponseDtoList.add(InfoResponseDto.builder()
                            .name(usefulInfo.getName())
                            .address(usefulInfo.getAddress()).build());
                    break;
                case "짐배달":
                    bagInfoResponseDtoList.add(InfoResponseDto.builder()
                            .name(usefulInfo.getName())
                            .address(usefulInfo.getAddress())
                            .build());
                    break;
                case "콜택시":
                    taxiInfoResponseDtoList.add(InfoResponseDto.builder()
                            .region(usefulInfo.getRegion())
                            .name(usefulInfo.getName())
                            .address(usefulInfo.getAddress()).build());
                    break;
                default:
                    break;
            }
        }

        usefulInfoResponseDtoList.add(UsefulInfoResponseDto.builder()
                .category("대중교통")
                .info(publicInfoResponseDtoList).build());
        usefulInfoResponseDtoList.add(UsefulInfoResponseDto.builder()
                .category("짐배달")
                .info(bagInfoResponseDtoList).build());
        usefulInfoResponseDtoList.add(UsefulInfoResponseDto.builder()
                .category("콜택시")
                .info(taxiInfoResponseDtoList).build());

        return ResponseDto.success(usefulInfoResponseDtoList);
    }
}
