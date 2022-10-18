package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.TouristSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class TouristSpotController {

    private final TouristSpotService touristSpotService;

    @GetMapping("/touristspot")
    public ResponseDto<?> getTouristSpotList(@RequestParam(value="page") int page, @RequestParam(value = "region", required = false) String region) {
        return touristSpotService.getTouristSpotList(page, region);
    }

    @GetMapping("/touristspot/{id}")
    public ResponseDto<?> getTouristSpotDetail(@PathVariable Long id) {
        return touristSpotService.getTouristSpotDetail(id);
    }
}
