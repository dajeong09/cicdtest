package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.bookmark.TouristSpotBookmarkRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.TOURISTSPOT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TouristSpotBookmarkService {

    private final TouristSpotRepository touristSpotRepository;
    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final TokenProvider tokenProvider;


    public ResponseDto<?> createTouristSpotBookmark(@PathVariable Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        TouristSpot touristSpot = isPresentTouristSpot(id);
        if (null == touristSpot) {
            return ResponseDto.fail(TOURISTSPOT_NOT_FOUND);
        }
        TouristSpotBookmark checkBookmark = touristSpotBookmarkRepository.findByMemberAndTouristSpot(member, touristSpot);
        if (null != checkBookmark) {
            touristSpotBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build());
        }
        TouristSpotBookmark touristSpotBookmark = TouristSpotBookmark.builder()
                .member(member)
                .touristSpot(touristSpot)
                .build();

        touristSpotBookmarkRepository.save(touristSpotBookmark); // 즐겨 찾기
        return ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build());
    }

    @Transactional
    public TouristSpot isPresentTouristSpot(Long id) {
        Optional<TouristSpot> optionalTouristSpot = touristSpotRepository.findTouristSpotById(id);
        return optionalTouristSpot.orElse(null);
    }
}