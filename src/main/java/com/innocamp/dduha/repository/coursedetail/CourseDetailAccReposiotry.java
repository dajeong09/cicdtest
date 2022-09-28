package com.innocamp.dduha.repository.coursedetail;

import com.innocamp.dduha.model.Course;
import com.innocamp.dduha.model.CourseDetailAcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDetailAccReposiotry extends JpaRepository<CourseDetailAcc, Long> {
    CourseDetailAcc findCourseDetailAccByCourse(Course course);
}
