package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.response.TouristSpotResponseDto;
import com.innocamp.dduha.service.TouristSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TouristSpotController {

    private final TouristSpotService touristSpotService;

    @GetMapping("/touristspot")
    public List<TouristSpotResponseDto> getTouristSpotList(HttpServletRequest request) {
        return touristSpotService.getTouristSpotList(request);
    }

}
