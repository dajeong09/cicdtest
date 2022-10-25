package com.innocamp.dduha.domain.bookmark.repository;

import com.innocamp.dduha.domain.bookmark.model.TripBookmark;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripBookmarkRepository extends JpaRepository<TripBookmark, Long> {
    TripBookmark findByMemberAndTrip(Member member, Trip trip);
    int countTripBookmarkByMember(Member member);
    List<TripBookmark> findAllByMember(Member member);
}
