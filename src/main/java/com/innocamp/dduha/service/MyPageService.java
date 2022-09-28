package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.response.MyPageResponseDto;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.TouristSpotBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.TripBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TripBookmarkRepository tripBookmarkRepository;
    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;

    private final MemberService memberService;

    public ResponseDto<?> getMyBookmarkedList(HttpServletRequest request) {

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        int tripBookmarkNum = tripBookmarkRepository.countTripBookmarkByMember(member);
        int touristSpotBookmarkNum = touristSpotBookmarkRepository.countTouristSpotBookmarkByMember(member);
        int restaurantBookmarkNum = restaurantBookmarkRepository.countRestaurantBookmarkByMember(member);
        // 숙소 즐겨찾기 개수 추가 예정

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .tripBookmarkNum(tripBookmarkNum)
                .touristSpotBookmarkNum(touristSpotBookmarkNum)
                .restaurantBookmarkNum(restaurantBookmarkNum)
                .build();
        return ResponseDto.success(myPageResponseDto);
    }
}
