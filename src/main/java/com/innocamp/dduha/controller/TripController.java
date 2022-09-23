package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.TripRequestDto;
import com.innocamp.dduha.model.Trip;
import com.innocamp.dduha.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping("/auth/trip")
    public ResponseDto<?> createTrip(@RequestBody TripRequestDto requestDto, HttpServletRequest request) {
        return tripService.createTrip(requestDto, request);
    }

    @GetMapping("/auth/trip")
    public ResponseDto<?> getMyTrip(HttpServletRequest request) {  //사용자 검증 후 변경
      return tripService.getMyTrip(request);
    }
}
