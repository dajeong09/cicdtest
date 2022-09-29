package com.innocamp.dduha.repository.coursedetail;

import com.innocamp.dduha.model.course.Course;
import com.innocamp.dduha.model.course.CourseDetailSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailSpotRepository extends JpaRepository<CourseDetailSpot, Long> {
    List<CourseDetailSpot> findAllByCourse(Course course);
    void deleteAllByCourse(Course course);
}
