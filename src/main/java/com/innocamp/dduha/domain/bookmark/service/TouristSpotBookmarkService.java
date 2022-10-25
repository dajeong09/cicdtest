package com.innocamp.dduha.domain.bookmark.service;

import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
import com.innocamp.dduha.domain.place.touristspot.repository.TouristSpotRepository;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.bookmark.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.bookmark.model.TouristSpotBookmark;
import com.innocamp.dduha.domain.bookmark.repository.TouristSpotBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.TOURISTSPOT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TouristSpotBookmarkService {

    private final TouristSpotRepository touristSpotRepository;
    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final TokenProvider tokenProvider;

    public ResponseEntity<?> createTouristSpotBookmark(@PathVariable Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        TouristSpot touristSpot = touristSpotRepository.findTouristSpotById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(TOURISTSPOT_NOT_FOUND)));

        TouristSpotBookmark checkBookmark = touristSpotBookmarkRepository.findByMemberAndTouristSpot(member, touristSpot);
        if (null != checkBookmark) {
            touristSpotBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build()));
        }
        TouristSpotBookmark touristSpotBookmark = TouristSpotBookmark.builder()
                .member(member)
                .touristSpot(touristSpot)
                .build();

        touristSpotBookmarkRepository.save(touristSpotBookmark); // 즐겨 찾기
        return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build()));
    }

}