package com.innocamp.dduha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Course {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private int day;

    @JoinColumn(name = "trip_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Trip trip;

//    @JoinColumn(name = "acc_id", nullable = false)    //null이 가능하고 데이터가 지워지면 null로 변환
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Accommodation accommodation;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseDetailRest> courseDetailRests;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseDetailSpot> courseDetailSpots;



}
