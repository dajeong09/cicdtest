package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping("/accommodation")
    public ResponseDto<?> getAccommodationList() {
        return accommodationService.getAccommodationList();
    }

    @GetMapping("/accommodation/{id}")
    public ResponseDto<?> getAccommodationDetail(@PathVariable Long id) {
        return accommodationService.getAccommodationDetail(id);
    }
}
