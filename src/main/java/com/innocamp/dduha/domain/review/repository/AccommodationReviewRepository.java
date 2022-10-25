package com.innocamp.dduha.domain.review.repository;

import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.accommodation.model.Accommodation;
import com.innocamp.dduha.domain.review.model.AccommodationReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
    List<AccommodationReview> findAllByAccommodationOrderByCreatedAtDesc(Accommodation accommodation);
    List<AccommodationReview> findAllByMember(Member member);
}
