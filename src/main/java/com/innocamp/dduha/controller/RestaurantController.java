package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.response.RestaurantResponseDto;
import com.innocamp.dduha.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurant")
    public List<RestaurantResponseDto> getRestaurantList(HttpServletRequest request) {
        return restaurantService.getRestaurantList(request);
    }
}
