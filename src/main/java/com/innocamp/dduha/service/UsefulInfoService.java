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

    public ResponseDto<?> getUsefulInfo(String category) {

        List<UsefulInfo> usefulInfoList = usefulInfoRepository.findAllByCategory(category);

        List<UsefulInfoResponseDto> usefulInfoResponseDtoList = new ArrayList<>();
        List<InfoResponseDto> infoResponseDtoList = new ArrayList<>();

        for (UsefulInfo usefulInfo : usefulInfoList) {
            infoResponseDtoList.add(InfoResponseDto.builder()
                            .region(usefulInfo.getRegion())
                            .name(usefulInfo.getName())
                            .address(usefulInfo.getAddress()).build());
        }

        usefulInfoResponseDtoList.add(UsefulInfoResponseDto.builder()
                .category(category)
                .info(infoResponseDtoList).build());

        return ResponseDto.success(usefulInfoResponseDtoList);
    }
}
