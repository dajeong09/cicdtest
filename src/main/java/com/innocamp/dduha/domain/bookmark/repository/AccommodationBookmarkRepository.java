package com.innocamp.dduha.domain.bookmark.repository;

import com.innocamp.dduha.domain.place.accommodation.model.Accommodation;
import com.innocamp.dduha.domain.bookmark.model.AccommodationBookmark;
import com.innocamp.dduha.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationBookmarkRepository extends JpaRepository<AccommodationBookmark, Long> {
    AccommodationBookmark findByMemberAndAccommodation(Member member, Accommodation accommodation);
    int countAccommodationBookmarkByMember(Member member);
    List<AccommodationBookmark> findAllByMember(Member member);
    int countByAccommodation(Accommodation accommodation);
}
