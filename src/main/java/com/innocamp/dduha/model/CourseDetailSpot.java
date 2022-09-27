package com.innocamp.dduha.model;

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
public class CourseDetailSpot {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private int detailOrder;

    @JoinColumn(name = "course_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JoinColumn(name = "spot_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TouristSpot touristSpot;
}
