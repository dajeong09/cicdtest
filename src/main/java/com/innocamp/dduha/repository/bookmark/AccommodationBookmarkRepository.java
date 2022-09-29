package com.innocamp.dduha.repository.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.bookmark.AccommodationBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationBookmarkRepository extends JpaRepository<AccommodationBookmark, Long> {
    AccommodationBookmark findByMemberAndAccommodation(Member member, Accommodation accommodation);

    int countAccommodationBookmarkByMember(Member member);

    List<AccommodationBookmark> findAllByMember(Member member);
}
