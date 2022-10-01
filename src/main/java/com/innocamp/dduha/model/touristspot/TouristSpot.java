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
public class TouristSpot {

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
    private String region;      // enum 으로 바꿀까 고민

    @Column
    private double latitude;

    @Column
    private double longitude;

}
