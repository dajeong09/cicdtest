package com.innocamp.dduha.repository.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristSpotBookmarkRepository extends JpaRepository<TouristSpotBookmark,Long> {
    TouristSpotBookmark findByMemberAndTouristSpot(Member member, TouristSpot touristSpot);

    int countTouristSpotBookmarkByMember(Member member);

    List<TouristSpotBookmark> findAllByMember(Member member);
}
