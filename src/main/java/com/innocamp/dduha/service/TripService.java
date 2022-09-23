package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.TripRequestDto;
import com.innocamp.dduha.dto.response.TripResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import com.innocamp.dduha.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.innocamp.dduha.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createTrip(TripRequestDto requestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        LocalDate startAt = LocalDate.parse(requestDto.getStartAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate endAt = LocalDate.parse(requestDto.getEndAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Trip trip = Trip.builder()
                .title(requestDto.getTitle())
                .isPublic(requestDto.getIsPublic())
                .startAt(startAt)
                .endAt(endAt)
                .member(member)
                .build();

        tripRepository.save(trip);

        return ResponseDto.success(NULL);
    }

    public ResponseDto<?> getMyTrip(HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        List<TripResponseDto> tripResponseDtoList = new ArrayList<>();

        List<Trip> tripList = tripRepository.findAllByMember(member);

        for (Trip trip : tripList) {
            tripResponseDtoList.add(TripResponseDto.builder()
                    .id(trip.getId())
                    .title(trip.getTitle())
                    .isPublic(trip.getIsPublic())
                    .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))).build()
            );
        }
        return ResponseDto.success(tripResponseDtoList);
    }

}
