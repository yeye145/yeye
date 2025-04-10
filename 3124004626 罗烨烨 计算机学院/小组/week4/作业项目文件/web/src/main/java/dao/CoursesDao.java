package dao;

import pojo.Courses;
import pojo.Students;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface CoursesDao {
    void updateScoreInCourses(String courseName, Integer newScore) throws Exception;

    // 获取新的sql.courses信息，用于操作
    Set<Courses> getCourseSet() throws SQLException;

    // 获取新的课程信息，用于查询打印
    List<Courses> getCourseList() throws SQLException;

    void insertNewCourse(Integer score, Integer max, String information, String name) throws Exception;

    void deleteCourseByName(String courseName) throws Exception;

    void updateCourseAfterSelectCourse(Courses courses) throws Exception;

    void turnCourseNotSelect(Integer key) throws Exception;

    void turnCourseCanSelect(Integer key) throws Exception;

    void updateCourseAfterDropCourse(Courses courses) throws Exception;
}
