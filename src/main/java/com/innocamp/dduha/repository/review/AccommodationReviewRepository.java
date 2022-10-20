package com.innocamp.dduha.repository.review;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.review.AccommodationReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
    List<AccommodationReview> findAllByAccommodationOrderByCreatedAtDesc(Accommodation accommodation);

    List<AccommodationReview> findAllByMember(Member member);
}
