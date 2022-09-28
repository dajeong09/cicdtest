package com.innocamp.dduha.model.accommodation;

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
public class AccommodationImg {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @JoinColumn(name = "acc_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;
}
