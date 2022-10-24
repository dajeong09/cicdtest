package com.innocamp.dduha.domain.trip.model;

import com.innocamp.dduha.domain.place.touristspot.model.TouristSpot;
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

    public void postponeOrder() {
        this.detailOrder++;
    }
    public void advanceOrder() {
        this.detailOrder--;
    }

    public void changeOrder(int detailOrder) {
        this.detailOrder = detailOrder;
    }
}
