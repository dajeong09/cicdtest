package com.innocamp.dduha.domain.bookmark.service;

import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.bookmark.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.trip.model.Trip;
import com.innocamp.dduha.domain.bookmark.model.TripBookmark;
import com.innocamp.dduha.domain.trip.repository.TripRepository;
import com.innocamp.dduha.domain.bookmark.repository.TripBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.TRIP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TripBookmarkService {

    private final TripBookmarkRepository tripBookmarkRepository;
    private final TokenProvider tokenProvider;
    private final TripRepository tripRepository;

    @Transactional
    public ResponseEntity<?> createTripBookmark(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Trip trip = tripRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(TRIP_NOT_FOUND)));

        TripBookmark checkBookmark = tripBookmarkRepository.findByMemberAndTrip(member, trip);
        if (null != checkBookmark) {
            tripBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build()));
        }
        TripBookmark tripBookmark = TripBookmark.builder()
                .member(member)
                .trip(trip)
                .build();

        tripBookmarkRepository.save(tripBookmark); // 즐겨 찾기
        return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build()));
    }

}
