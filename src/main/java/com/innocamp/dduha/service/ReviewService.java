package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.ReviewRequestDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.review.AccommodationReview;
import com.innocamp.dduha.model.review.RestaurantReview;
import com.innocamp.dduha.model.review.TouristSpotReview;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import com.innocamp.dduha.repository.review.AccommodationReviewRepository;
import com.innocamp.dduha.repository.review.RestaurantReviewRepository;
import com.innocamp.dduha.repository.review.TouristSpotReviewRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final TokenProvider tokenProvider;
    private final TouristSpotRepository touristSpotRepository;
    private final TouristSpotReviewRepository touristSpotReviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantReviewRepository restaurantReviewRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationReviewRepository accommodationReviewRepository;

    public ResponseDto<?> createReview(String category, Long id, ReviewRequestDto requestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        switch (category) {
            case "touristspot":
                TouristSpot touristSpot = isPresentTouristSpot(id);
                if (null == touristSpot) {
                    return ResponseDto.fail(TOURISTSPOT_NOT_FOUND);
                }
                TouristSpotReview touristSpotReview = TouristSpotReview.builder()
                        .member(member)
                        .touristSpot(touristSpot)
                        .review(requestDto.getReview()).build();

                touristSpotReviewRepository.save(touristSpotReview);

                break;
            case "restaurant":
                Restaurant restaurant = isPresentRestaurant(id);
                if (null == restaurant) {
                    return ResponseDto.fail(RESTAURANT_NOT_FOUND);
                }
                RestaurantReview restaurantReview = RestaurantReview.builder()
                        .member(member)
                        .restaurant(restaurant)
                        .review(requestDto.getReview()).build();

                restaurantReviewRepository.save(restaurantReview);

                break;
            case "accommodation":
                Accommodation accommodation = isPresentAccommodation(id);
                if (null == accommodation) {
                    return ResponseDto.fail(ACCOMMODATION_NOT_FOUND);
                }
                AccommodationReview accommodationReview = AccommodationReview.builder()
                        .member(member)
                        .accommodation(accommodation)
                        .review(requestDto.getReview()).build();

                accommodationReviewRepository.save(accommodationReview);

                break;
            default:
                return ResponseDto.fail(INVALID_CATEGORY);
        }

        return ResponseDto.success(NULL);
    }

    public ResponseDto<?> updateReview(String category, Long id, ReviewRequestDto requestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        switch (category) {
            case "touristspot":
                TouristSpotReview touristSpotReview = isPresentTouristSpotReview(id);
                if (null == touristSpotReview) {
                    return ResponseDto.fail(REVIEW_NOT_FOUND);
                }
                if (touristSpotReview.getMember().getId() != member.getId()) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                touristSpotReview.update(requestDto.getReview());
                touristSpotReviewRepository.save(touristSpotReview);
                break;
            case "restaurant":
                RestaurantReview restaurantReview = isPresentRestaurantReview(id);
                if (null == restaurantReview) {
                    return ResponseDto.fail(REVIEW_NOT_FOUND);
                }
                if (restaurantReview.getMember().getId() != member.getId()) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                restaurantReview.update(requestDto.getReview());
                restaurantReviewRepository.save(restaurantReview);
                break;
            case "accommodation":
                AccommodationReview accommodationReview = isPresentAccommodationReview(id);
                if (null == accommodationReview) {
                    return ResponseDto.fail(REVIEW_NOT_FOUND);
                }
                if (accommodationReview.getMember().getId() != member.getId()) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                accommodationReview.update(requestDto.getReview());
                accommodationReviewRepository.save(accommodationReview);
                break;
            default:
                return ResponseDto.fail(INVALID_CATEGORY);
        }

        return ResponseDto.success(NULL);
    }

    public ResponseDto deleteReview(String category, Long id, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        switch (category) {
            case "touristspot":
                TouristSpotReview touristSpotReview = isPresentTouristSpotReview(id);
                if (null == touristSpotReview) {
                    return ResponseDto.fail(REVIEW_NOT_FOUND);
                }
                if (touristSpotReview.getMember().getId() != member.getId()) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                touristSpotReviewRepository.delete(touristSpotReview);
                break;
            case "restaurant":
                RestaurantReview restaurantReview = isPresentRestaurantReview(id);
                if (null == restaurantReview) {
                    return ResponseDto.fail(REVIEW_NOT_FOUND);
                }
                if (restaurantReview.getMember().getId() != member.getId()) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                restaurantReviewRepository.delete(restaurantReview);
                break;
            case "accommodation":
                AccommodationReview accommodationReview = isPresentAccommodationReview(id);
                if (null == accommodationReview) {
                    return ResponseDto.fail(REVIEW_NOT_FOUND);
                }
                if (accommodationReview.getMember().getId() != member.getId()) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                accommodationReviewRepository.delete(accommodationReview);
                break;
            default:
                return ResponseDto.fail(INVALID_CATEGORY);
        }

        return ResponseDto.success(NULL);
    }

    public TouristSpot isPresentTouristSpot(Long id) {
        Optional<TouristSpot> optionalTouristSpot = touristSpotRepository.findById(id);
        return optionalTouristSpot.orElse(null);
    }

    public Restaurant isPresentRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }

    public Accommodation isPresentAccommodation(Long id) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(id);
        return optionalAccommodation.orElse(null);
    }

    public TouristSpotReview isPresentTouristSpotReview(Long id) {
        Optional<TouristSpotReview> optionalTouristSpotReview = touristSpotReviewRepository.findById(id);
        return optionalTouristSpotReview.orElse(null);
    }

    public RestaurantReview isPresentRestaurantReview(Long id) {
        Optional<RestaurantReview> optionalRestaurantReview = restaurantReviewRepository.findById(id);
        return optionalRestaurantReview.orElse(null);
    }

    public AccommodationReview isPresentAccommodationReview(Long id) {
        Optional<AccommodationReview> optionalAccommodationReview = accommodationReviewRepository.findById(id);
        return optionalAccommodationReview.orElse(null);
    }

}