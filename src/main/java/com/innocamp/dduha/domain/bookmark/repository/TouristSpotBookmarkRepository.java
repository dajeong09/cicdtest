package com.innocamp.dduha.domain.bookmark.repository;

import com.innocamp.dduha.domain.bookmark.model.TouristSpotBookmark;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotBookmarkRepository extends JpaRepository<TouristSpotBookmark,Long> {
    TouristSpotBookmark findByMemberAndTouristSpot(Member member, TouristSpot touristSpot);
    int countTouristSpotBookmarkByMember(Member member);
    List<TouristSpotBookmark> findAllByMember(Member member);
    int countByTouristSpot(TouristSpot touristSpot);
}
