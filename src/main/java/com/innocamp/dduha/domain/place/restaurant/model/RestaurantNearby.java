package com.innocamp.dduha.domain.place.restaurant.model;

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
public class RestaurantNearby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "rest_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @JoinColumn(name = "bus_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusStation busStation;

    @Column
    private int distance;
}
