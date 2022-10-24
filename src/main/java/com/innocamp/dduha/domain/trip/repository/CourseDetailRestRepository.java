package com.innocamp.dduha.domain.trip.repository;

import com.innocamp.dduha.domain.trip.model.Course;
import com.innocamp.dduha.domain.trip.model.CourseDetailRest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailRestRepository extends JpaRepository<CourseDetailRest, Long> {
    List<CourseDetailRest> findAllByCourse(Course course);
    List<CourseDetailRest> findAllByCourseAndDetailOrderGreaterThanEqual(Course course, int detailOrder);
    int countAllByCourse(Course course);
}
