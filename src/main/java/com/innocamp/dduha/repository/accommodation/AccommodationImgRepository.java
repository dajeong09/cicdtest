package com.innocamp.dduha.repository.accommodation;

import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.accommodation.AccommodationImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationImgRepository extends JpaRepository<AccommodationImg, Long> {
    List<AccommodationImg> findAllByAccommodation(Accommodation accommodation);
}
