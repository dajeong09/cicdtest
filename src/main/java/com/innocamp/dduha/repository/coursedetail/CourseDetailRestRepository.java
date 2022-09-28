package com.innocamp.dduha.repository.coursedetail;

import com.innocamp.dduha.model.Course;
import com.innocamp.dduha.model.CourseDetailRest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailRestRepository extends JpaRepository<CourseDetailRest, Long> {
    List<CourseDetailRest> findAllByCourse(Course course);
}
