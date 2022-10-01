package com.innocamp.dduha.repository.coursedetail;

import com.innocamp.dduha.model.course.Course;
import com.innocamp.dduha.model.course.CourseDetailAcc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDetailAccReposiotry extends JpaRepository<CourseDetailAcc, Long> {
    CourseDetailAcc findCourseDetailAccByCourse(Course course);
    List<CourseDetailAcc> findAllByCourseAndDetailOrderGreaterThanEqual(Course course, int detailOrder);
    void deleteAllByCourse(Course course);
    int countAllByCourse(Course course);
}
