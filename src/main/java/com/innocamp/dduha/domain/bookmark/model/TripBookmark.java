package com.innocamp.dduha.domain.bookmark.model;

import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.global.common.Timestamped;
import com.innocamp.dduha.domain.trip.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TripBookmark extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "trip_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

}
