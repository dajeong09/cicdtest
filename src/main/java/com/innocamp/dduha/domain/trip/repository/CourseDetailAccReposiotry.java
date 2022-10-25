package com.innocamp.dduha.domain.trip.repository;

import com.innocamp.dduha.domain.trip.model.CourseDetailAcc;
import com.innocamp.dduha.domain.trip.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailAccReposiotry extends JpaRepository<CourseDetailAcc, Long> {
    List<CourseDetailAcc> findAllByCourse(Course course);
    List<CourseDetailAcc> findAllByCourseAndDetailOrderGreaterThanEqual(Course course, int detailOrder);
    int countAllByCourse(Course course);
}
