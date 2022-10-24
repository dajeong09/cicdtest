package com.innocamp.dduha.domain.information.model;

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
public class UsefulInfo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column
    private String region;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;
}
