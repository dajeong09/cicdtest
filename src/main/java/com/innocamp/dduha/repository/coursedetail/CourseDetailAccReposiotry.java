package com.innocamp.dduha.repository.coursedetail;

import com.innocamp.dduha.model.course.Course;
import com.innocamp.dduha.model.course.CourseDetailAcc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDetailAccReposiotry extends JpaRepository<CourseDetailAcc, Long> {
    CourseDetailAcc findCourseDetailAccByCourse(Course course);
    void deleteAllByCourse(Course course);
}
