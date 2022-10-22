package com.innocamp.dduha.controller;

import com.innocamp.dduha.service.TideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TideController {

    private final TideService tideService;

    @GetMapping("/tide")
    public ResponseEntity<?> getTide(@RequestParam(value="obs", required = false) String obs) {
        return tideService.getTide(obs);
    }
}
