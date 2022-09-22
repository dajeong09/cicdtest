package com.innocamp.dduha.model.touristspot;

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
public class TouristSpotImg {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @JoinColumn(name = "spot_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TouristSpot touristSpot;
}
