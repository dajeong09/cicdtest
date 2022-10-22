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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.validation.ValidationException;
import java.util.NoSuchElementException;

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

    public ResponseEntity<?> updateReview(String category, Long id, ReviewRequestDto requestDto) throws AuthenticationException {

        Member member = tokenProvider.getMemberFromAuthentication();

        switch (category) {
            case "touristspot":
                TouristSpotReview touristSpotReview = touristSpotReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (touristSpotReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                touristSpotReview.update(requestDto.getReview());
                touristSpotReviewRepository.save(touristSpotReview);
                break;
            case "restaurant":
                RestaurantReview restaurantReview = restaurantReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (restaurantReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                restaurantReview.update(requestDto.getReview());
                restaurantReviewRepository.save(restaurantReview);
                break;
            case "accommodation":
                AccommodationReview accommodationReview = accommodationReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));
                if (accommodationReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                accommodationReview.update(requestDto.getReview());
                accommodationReviewRepository.save(accommodationReview);
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    public ResponseEntity<?> deleteReview(String category, Long id) throws AuthenticationException {

        Member member = tokenProvider.getMemberFromAuthentication();

        switch (category) {
            case "touristspot":
                TouristSpotReview touristSpotReview = touristSpotReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (touristSpotReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                touristSpotReviewRepository.delete(touristSpotReview);
                break;
            case "restaurant":
                RestaurantReview restaurantReview = restaurantReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));

                if (restaurantReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                restaurantReviewRepository.delete(restaurantReview);
                break;
            case "accommodation":
                AccommodationReview accommodationReview = accommodationReviewRepository.findById(id).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(REVIEW_NOT_FOUND)));
                if (accommodationReview.getMember().getId() != member.getId()) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                accommodationReviewRepository.delete(accommodationReview);
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

}