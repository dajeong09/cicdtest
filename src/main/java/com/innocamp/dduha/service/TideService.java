package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.TideResponseDto;
import com.innocamp.dduha.model.Tide;
import com.innocamp.dduha.repository.TideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TideService {

    private final TideRepository tideRepository;

    public ResponseDto<?> getTide(String obs) {

        LocalDate now = LocalDate.now();
        List<TideResponseDto> tideResponseDtoList = new ArrayList<>();

        for(int i = 0; i <7; i++) {
            LocalDate getDate = now.plusDays(i);
            List<Tide> tideList = tideRepository.findAllByDateAndObservatory(getDate, obs);

            for(Tide tide : tideList) {
                String date = tide.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                List<String> dataList = List.of(tide.getData().substring(0,tide.getData().length()-1).replace(date+" ","").replace("{","").split("},"));

                tideResponseDtoList.add(TideResponseDto.builder()
                        .date(date)
                        .tide(dataList).build());
            }
        }

        return ResponseDto.success(tideResponseDtoList);
    }
}
