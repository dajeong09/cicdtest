package com.innocamp.dduha.domain.information.service;

import com.innocamp.dduha.domain.information.dto.response.TideDetailResponseDto;
import com.innocamp.dduha.domain.information.dto.response.TideResponseDto;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.information.model.Tide;
import com.innocamp.dduha.domain.information.repository.TideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TideService {

    private final TideRepository tideRepository;

    public ResponseEntity<?> getTide(String obs) {

        LocalDate now = LocalDate.now();
        List<TideResponseDto> tideResponseDtoList = new ArrayList<>();

        for(int i = 0; i <7; i++) {
            LocalDate getDate = now.plusDays(i);
            List<Tide> tideList = tideRepository.findAllByDateAndObservatory(getDate, obs);

            for(Tide tide : tideList) {
                String date = tide.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                List<String> dataList = List.of(tide.getData().substring(0,tide.getData().length()-1).replace(date+" ","").replace("{","").split("},"));
                List<TideDetailResponseDto> tideDetailResponseDtoList = new ArrayList<>();
                for(String data : dataList) {
                    String[] dataDetails = data.split(",");
                    tideDetailResponseDtoList.add(TideDetailResponseDto.builder()
                                    .level(dataDetails[0].substring(dataDetails[0].indexOf(":")+1))
                                    .time(dataDetails[1].substring(dataDetails[1].indexOf(":")+1))
                                    .code(dataDetails[2].substring(dataDetails[2].indexOf(":")+1)).build());
                }
                tideResponseDtoList.add(TideResponseDto.builder()
                                .observatory(tide.getObservatory())
                                .date(date)
                                .tide(tideDetailResponseDtoList).build());
            }
        }

        return ResponseEntity.ok(ResponseDto.success(tideResponseDtoList));
    }
}
