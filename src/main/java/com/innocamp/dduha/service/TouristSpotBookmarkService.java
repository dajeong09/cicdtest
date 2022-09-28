package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.bookmark.TouristSpotBookmarkRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TouristSpotBookmarkService {

    private final TouristSpotRepository touristSpotRepository;

    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;

    private final MemberService memberService;


    public ResponseDto<?> createTouristSpotBookmark(@PathVariable Long spotId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request); // 둘 중 하나만 해도 되지 않을까
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }
        TouristSpot touristSpot = isPresentTouristSpot(spotId);
        if (null == touristSpot) {
            return ResponseDto.fail(SPOT_NOT_FOUND);
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