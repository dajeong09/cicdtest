package com.innocamp.dduha.repository.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristSpotBookmarkRepository extends JpaRepository<TouristSpotBookmark,Long> {
    TouristSpotBookmark findByMemberAndTouristSpot(Member member, TouristSpot touristSpot);

    int countTouristSpotBookmarkByMember(Member member);
}
