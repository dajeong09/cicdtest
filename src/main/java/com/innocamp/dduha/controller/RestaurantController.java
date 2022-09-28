package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurant")
    public ResponseDto<?> getRestaurantList() {
        return restaurantService.getRestaurantList();
    }
}
