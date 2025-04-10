package dao;

import pojo.Courses;
import pojo.StudentCourses;
import pojo.Students;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface StudentCoursesDao {
    void updateScoreInStudentCourses(String courseName, Integer newScore) throws Exception;

    // 获取新的sql.student_courses信息，用于操作
    Set<StudentCourses> getStudentCourseSet() throws SQLException;

    // 获取新的课程信息，用于查询打印
    List<StudentCourses> getStudentCourseList() throws SQLException;

    void insertNewStudentCourse(Integer score, Integer max, String information, String name) throws Exception;

    void deleteStudentCourseByName(String courseName) throws Exception;

    void insertStudentCourseAfterDropCourse(Courses c) throws Exception;
}
