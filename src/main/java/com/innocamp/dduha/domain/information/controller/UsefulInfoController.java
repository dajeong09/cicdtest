package com.innocamp.dduha.domain.information.controller;

import com.innocamp.dduha.domain.information.service.UsefulInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsefulInfoController {

    private final UsefulInfoService usefulInfoService;

    @GetMapping("/information")
    public ResponseEntity<?> getUsefulInfo(@RequestParam(value="category") String category) {
        return usefulInfoService.getUsefulInfo(category);
    }
}
