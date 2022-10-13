package com.innocamp.dduha.model.bookmark;

import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.Timestamped;
import com.innocamp.dduha.model.restaurant.Restaurant;
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
public class RestaurantBookmark extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "rest_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

}
