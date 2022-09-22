package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.request.TripRequestDto;
import com.innocamp.dduha.model.Trip;
import com.innocamp.dduha.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    @Transactional
    public void createTrip(TripRequestDto requestDto, HttpServletRequest request) {
        //로그인 확인 필요

        LocalDate startAt = LocalDate.parse(requestDto.getStartAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate endAt = LocalDate.parse(requestDto.getEndAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Trip trip = Trip.builder()
                .title(requestDto.getTitle())
                .isPublic(requestDto.getIsPublic())
                .startAt(startAt)
                .endAt(endAt)
                //.member(member)
                .build();

        tripRepository.save(trip);
    }

    public List<Trip> getMyTrip(HttpServletRequest request) {
        //사용자 검증

        return tripRepository.findAll();     // findAllByMember 로 변경

    }


}
