package com.innocamp.dduha.service.bookmark;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.BookmarkResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.bookmark.AccommodationBookmark;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.bookmark.AccommodationBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.innocamp.dduha.exception.ErrorCode.ACCOMMODATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccommodationBookmarkService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationBookmarkRepository accommodationBookmarkRepository;
    private final TokenProvider tokenProvider;

    public ResponseEntity<?> createAccommodationBookmark(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(ACCOMMODATION_NOT_FOUND)));

        AccommodationBookmark checkBookmark = accommodationBookmarkRepository.findByMemberAndAccommodation(member, accommodation);
        if (null != checkBookmark) {
            accommodationBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                    .isBookmarked(false)
                    .build()));
        }
        AccommodationBookmark accommodationBookmark = AccommodationBookmark.builder()
                .member(member)
                .accommodation(accommodation)
                .build();

        accommodationBookmarkRepository.save(accommodationBookmark); // 즐겨 찾기
        return ResponseEntity.ok(ResponseDto.success(BookmarkResponseDto.builder()
                .isBookmarked(true)
                .build()));
    }

}
