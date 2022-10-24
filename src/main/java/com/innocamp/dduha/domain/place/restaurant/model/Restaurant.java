package com.innocamp.dduha.domain.place.restaurant.model;

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
public class Restaurant {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column
    private String phone;

    @Column
    private String info;

    @Column
    private int likeNum;

    @Column(nullable = false)
    private String region;

    @Column
    private double latitude;

    @Column
    private double longitude;
}
