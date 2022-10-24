package com.innocamp.dduha.domain.information.service;

import com.innocamp.dduha.domain.information.model.UsefulInfo;
import com.innocamp.dduha.domain.information.repository.UsefulInfoRepository;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.information.dto.response.InfoResponseDto;
import com.innocamp.dduha.domain.information.dto.response.UsefulInfoResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UsefulInfoService {

    private final UsefulInfoRepository usefulInfoRepository;

    public ResponseEntity<?> getUsefulInfo(String category) {

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

        return ResponseEntity.ok(ResponseDto.success(usefulInfoResponseDtoList));
    }
}
