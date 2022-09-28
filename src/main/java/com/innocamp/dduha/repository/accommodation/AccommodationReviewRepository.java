package com.innocamp.dduha.repository.accommodation;

import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.accommodation.AccommodationReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
    List<AccommodationReview> findAllByAccommodationOrderByReviewedAtDesc(Accommodation accommodation);
}
