package com.innocamp.dduha.model.touristspot;

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
public class TouristSpotReview {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String reviewer;

    @Column(nullable = false)
    private String review;

    @Column(nullable = false)
    private LocalDate reviewedAt;

    @JoinColumn(name = "spot_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TouristSpot touristSpot;

}
