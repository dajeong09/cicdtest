package com.innocamp.dduha.domain.place.accommodation.repository;

import com.innocamp.dduha.domain.place.accommodation.model.Accommodation;
import com.innocamp.dduha.domain.place.accommodation.model.AccommodationNearby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationNearbyRepository extends JpaRepository<AccommodationNearby, Long> {
    List<AccommodationNearby> findAllByAccommodation(Accommodation accommodation);
}
