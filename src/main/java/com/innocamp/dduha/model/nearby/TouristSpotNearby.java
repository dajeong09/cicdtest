package com.innocamp.dduha.model.nearby;

import com.innocamp.dduha.model.BusStation;
import com.innocamp.dduha.model.touristspot.TouristSpot;
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
public class TouristSpotNearby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "spot_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TouristSpot touristSpot;

    @JoinColumn(name = "bus_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusStation busStation;

    @Column
    private int distance;

}
