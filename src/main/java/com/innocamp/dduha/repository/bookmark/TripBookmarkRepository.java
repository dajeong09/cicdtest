package com.innocamp.dduha.repository.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
import com.innocamp.dduha.model.bookmark.TripBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripBookmarkRepository extends JpaRepository<TripBookmark, Long> {
    TripBookmark findByMemberAndTrip(Member member, Trip trip);

    int countTripBookmarkByMember(Member member);

}