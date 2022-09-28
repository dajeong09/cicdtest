package com.innocamp.dduha.model.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Trip;
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
public class TripBookmark {

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
