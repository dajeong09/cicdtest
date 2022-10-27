package com.innocamp.dduha.domain.review.service;

import com.innocamp.dduha.domain.place.restaurant.model.Restaurant;
import com.innocamp.dduha.domain.place.restaurant.repository.RestaurantRepository;
import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
import com.innocamp.dduha.domain.place.touristspot.repository.TouristSpotRepository;
import com.innocamp.dduha.domain.review.dto.request.ReviewRequestDto;
import com.innocamp.dduha.domain.review.repository.AccommodationReviewRepository;
import com.innocamp.dduha.domain.review.repository.RestaurantReviewRepository;
import com.innocamp.dduha.domain.review.repository.TouristSpotReviewRepository;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.accommodation.model.Accommodation;
import com.innocamp.dduha.domain.review.model.AccommodationReview;
import com.innocamp.dduha.domain.review.model.RestaurantReview;
import com.innocamp.dduha.domain.review.model.TouristSpotReview;
import com.innocamp.dduha.domain.place.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.NoSuchElementException;

import static com.innocamp.dduha.global.exception.ErrorCode.*;

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

    public ResponseEntity<?> createReview(String category, Long id, ReviewRequestDto requestDto) {

        Member member = tokenProvider.getMemberFromAuthentication();

        switch (category) {
            case "touristspot":
                TouristSpot touristSpot = touristSpotRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(TOURISTSPOT_NOT_FOUND)));

                TouristSpotReview touristSpotReview = TouristSpotReview.builder()
                        .member(member)
                        .touristSpot(touristSpot)
                        .review(requestDto.getReview()).build();

                touristSpotReviewRepository.save(touristSpotReview);
                break;
            case "restaurant":
                Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(RESTAURANT_NOT_FOUND)));

                RestaurantReview restaurantReview = RestaurantReview.builder()
                        .member(member)
                        .restaurant(restaurant)
                        .review(requestDto.getReview()).build();

                restaurantReviewRepository.save(restaurantReview);
                break;
            case "accommodation":
                Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(ACCOMMODATION_NOT_FOUND)));

                AccommodationReview accommodationReview = AccommodationReview.builder()
                        .member(member)
                        .accommodation(accommodation)
                        .review(requestDto.getReview()).build();

                accommodationReviewRepository.save(accommodationReview);
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    public ResponseEntity<?> updateReview(String category, Long id, ReviewRequestDto requestDto) {

        Member member = tokenProvider.getMemberFromAuthentication();

        switch (category) {
            case "touristspot":
                TouristSpotReview touristSpotReview = touristSpotReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (touristSpotReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                }
                touristSpotReview.update(requestDto.getReview());
                touristSpotReviewRepository.save(touristSpotReview);
                break;
            case "restaurant":
                RestaurantReview restaurantReview = restaurantReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (restaurantReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                }
                restaurantReview.update(requestDto.getReview());
                restaurantReviewRepository.save(restaurantReview);
                break;
            case "accommodation":
                AccommodationReview accommodationReview = accommodationReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));
                if (accommodationReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                }
                accommodationReview.update(requestDto.getReview());
                accommodationReviewRepository.save(accommodationReview);
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    public ResponseEntity<?> deleteReview(String category, Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        switch (category) {
            case "touristspot":
                TouristSpotReview touristSpotReview = touristSpotReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (touristSpotReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                }
                touristSpotReviewRepository.delete(touristSpotReview);
                break;
            case "restaurant":
                RestaurantReview restaurantReview = restaurantReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (restaurantReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                }
                restaurantReviewRepository.delete(restaurantReview);
                break;
            case "accommodation":
                AccommodationReview accommodationReview = accommodationReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));
                if (accommodationReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                }
                accommodationReviewRepository.delete(accommodationReview);
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

}