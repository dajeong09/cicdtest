package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurant")
    public ResponseDto<?> getRestaurantList(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "region", required = false) String region,
            @RequestParam(value = "station", required = false) String station) {
        return restaurantService.getRestaurantList(page, region, station);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseDto<?> getRestaurantDetail(@PathVariable Long id) {
        return restaurantService.getRestaurantDetail(id);
    }
}
