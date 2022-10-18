package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import com.innocamp.dduha.model.bookmark.TripBookmark;
import com.innocamp.dduha.repository.TripRepository;
import com.innocamp.dduha.repository.bookmark.TripBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.TRIP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TripBookmarkService {

    private final TripBookmarkRepository tripBookmarkRepository;
    private final TokenProvider tokenProvider;
    private final TripRepository tripRepository;

    @Transactional
    public ResponseDto<?> createTripBookmark(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Trip trip = isPresentTrip(id);
        if (null == trip) {
            return ResponseDto.fail(TRIP_NOT_FOUND);
        }
        TripBookmark checkBookmark = tripBookmarkRepository.findByMemberAndTrip(member, trip);
        if (null != checkBookmark) {
            tripBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build());
        }
        TripBookmark tripBookmark = TripBookmark.builder()
                .member(member)
                .trip(trip)
                .build();

        tripBookmarkRepository.save(tripBookmark); // 즐겨 찾기
        return ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build());
    }

    @Transactional
    public Trip isPresentTrip(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        return optionalTrip.orElse(null);
    }
}
