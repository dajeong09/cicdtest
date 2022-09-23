package com.innocamp.dduha.model.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RestaurantReview {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String reviewer;

    @Column(nullable = false)
    private String review;

    @Column(nullable = false)
    private LocalDate reviewedAt;

    @JoinColumn(name = "rest_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

}
