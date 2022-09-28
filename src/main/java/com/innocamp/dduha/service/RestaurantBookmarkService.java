package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class RestaurantBookmarkService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseDto<?> createRestaurantBookmark(Long restId, HttpServletRequest request) {
        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        Member member = memberService.validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }
        Restaurant restaurant = isPresentRestaurant(restId);
        if (null == restaurant) {
            return ResponseDto.fail(RESTAURANT_NOT_FOUND);
        }
        RestaurantBookmark checkBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
        if (null != checkBookmark) {
            restaurantBookmarkRepository.delete(checkBookmark); // 즐겨 찾기 취소
            return ResponseDto.success(NULL);
        }
        RestaurantBookmark restaurantBookmark = RestaurantBookmark.builder()
                .member(member)
                .restaurant(restaurant)
                .build();

        restaurantBookmarkRepository.save(restaurantBookmark); // 즐겨 찾기
        return ResponseDto.success(NULL);
    }

    @Transactional
    public Restaurant isPresentRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }
}
