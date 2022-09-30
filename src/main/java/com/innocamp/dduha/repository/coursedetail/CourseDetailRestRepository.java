package com.innocamp.dduha.repository.coursedetail;

import com.innocamp.dduha.model.course.Course;
import com.innocamp.dduha.model.course.CourseDetailRest;
import com.innocamp.dduha.model.course.CourseDetailSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailRestRepository extends JpaRepository<CourseDetailRest, Long> {
    List<CourseDetailRest> findAllByCourse(Course course);
    List<CourseDetailRest> findAllByCourseAndDetailOrderGreaterThanEqual(Course course, int detailOrder);
    void deleteAllByCourse(Course course);
}
