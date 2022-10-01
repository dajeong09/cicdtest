package com.innocamp.dduha.model;

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
public class BusStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stationId;

    @Column
    private String stationName;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private String stationInfo;
}
