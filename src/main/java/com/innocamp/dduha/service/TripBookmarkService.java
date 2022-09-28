package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import com.innocamp.dduha.model.bookmark.TripBookmark;
import com.innocamp.dduha.repository.TripRepository;
import com.innocamp.dduha.repository.bookmark.TripBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TripBookmarkService {

    private final TripBookmarkRepository tripBookmarkRepository;

    private final MemberService memberService;

    private final TripRepository tripRepository;

    @Transactional
    public ResponseDto<?> createTripBookmark(Long tripId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN); // 둘 중 하나만 해도 되지 않을까
        }
        Trip trip = isPresentTrip(tripId);
        if (null == trip) {
            return ResponseDto.fail(TRIP_NOT_FOUND);
        }
        TripBookmark checkBookmark = tripBookmarkRepository.findByMemberAndTrip(member, trip);
        if (null != checkBookmark) {
            tripBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseDto.success(NULL);
        }
        TripBookmark tripBookmark = TripBookmark.builder()
                .member(member)
                .trip(trip)
                .build();

        tripBookmarkRepository.save(tripBookmark); // 즐겨 찾기
        return ResponseDto.success(NULL);
    }

    @Transactional
    public Trip isPresentTrip(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        return optionalTrip.orElse(null);
    }
}
