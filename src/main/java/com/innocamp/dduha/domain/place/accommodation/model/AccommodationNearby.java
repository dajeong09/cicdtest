package com.innocamp.dduha.domain.place.accommodation.model;

import com.innocamp.dduha.domain.place.bustation.model.BusStation;
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
public class AccommodationNearby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "acc_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @JoinColumn(name = "bus_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusStation busStation;

    @Column
    private int distance;

}
