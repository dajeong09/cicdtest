package com.innocamp.dduha.domain.trip.repository;

import com.innocamp.dduha.domain.trip.model.Trip;
import com.innocamp.dduha.domain.trip.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByTrip(Trip trip);
    void deleteByTripAndDayAfter(Trip trip, int day);
}
