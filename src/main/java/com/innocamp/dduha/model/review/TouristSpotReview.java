package com.innocamp.dduha.model.review;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Timestamped;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TouristSpotReview extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String review;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "spot_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TouristSpot touristSpot;

    public void update(String review) {
        this.review = review;
    }

}
