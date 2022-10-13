package com.innocamp.dduha.model.review;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Timestamped;
import com.innocamp.dduha.model.accommodation.Accommodation;
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
public class AccommodationReview extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String review;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "acc_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    public void update(String review) {
        this.review = review;
    }
}
