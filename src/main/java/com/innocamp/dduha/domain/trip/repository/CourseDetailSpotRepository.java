package com.innocamp.dduha.domain.trip.repository;

import com.innocamp.dduha.domain.trip.model.Course;
import com.innocamp.dduha.domain.trip.model.CourseDetailSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailSpotRepository extends JpaRepository<CourseDetailSpot, Long> {
    List<CourseDetailSpot> findAllByCourse(Course course);
    List<CourseDetailSpot> findAllByCourseAndDetailOrderGreaterThanEqual(Course course, int detailOrder);
    int countAllByCourse(Course course);
}
