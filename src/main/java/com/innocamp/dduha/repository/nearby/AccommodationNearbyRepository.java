package com.innocamp.dduha.repository.nearby;

import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.nearby.AccommodationNearby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationNearbyRepository extends JpaRepository<AccommodationNearby, Long> {
    List<AccommodationNearby> findAllByAccommodation(Accommodation accommodation);
}
