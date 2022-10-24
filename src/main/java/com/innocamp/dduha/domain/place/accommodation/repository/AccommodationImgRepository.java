package com.innocamp.dduha.domain.place.accommodation.repository;

import com.innocamp.dduha.domain.place.accommodation.model.Accommodation;
import com.innocamp.dduha.domain.place.accommodation.model.AccommodationImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationImgRepository extends JpaRepository<AccommodationImg, Long> {
    List<AccommodationImg> findAllByAccommodation(Accommodation accommodation);
}
