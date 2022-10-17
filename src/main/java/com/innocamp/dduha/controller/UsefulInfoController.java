package com.innocamp.dduha.controller;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.service.UsefulInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsefulInfoController {

    private final UsefulInfoService usefulInfoService;

    @GetMapping("/information")
    public ResponseDto<?> getUsefulInfo(@RequestParam(value="category") String category) {
        return usefulInfoService.getUsefulInfo(category);
    }
}
