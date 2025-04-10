package dao.impl;

import dao.StudentCoursesDao;
import dao.utils.MySearch;
import dao.utils.MyUpdate;

import pojo.Courses;
import pojo.StudentCourses;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class StudentCoursesDaoImpl implements StudentCoursesDao {
    @Override
    public void updateScoreInStudentCourses(String courseName, Integer newScore) throws Exception {
        MyUpdate.update("UPDATE `student`.`student_courses` SET `score` = ? " +
                "WHERE (`courseName` = ?);", newScore, courseName);
    }


    // 获取新的sql.student_courses信息，用于操作
    @Override
    public Set<StudentCourses> getStudentCourseSet() throws SQLException {
        return MySearch.searchToSet("SELECT * FROM student.student_courses", StudentCourses.class);
    }


    // 获取新的课程信息，用于查询打印
    @Override
    public List<StudentCourses> getStudentCourseList() throws SQLException {
        return MySearch.searchToList("SELECT * FROM student.student_courses", StudentCourses.class);
    }


    @Override
    public void insertNewStudentCourse(Integer score, Integer max, String information, String name) throws Exception {
        String sql = "INSERT INTO `student`.`student_courses` " +
                "(`courseName`, `score`,`information`)" +
                " VALUES (?, ?, ?)";
        //更新选课表
        MyUpdate.update(sql, name, score, information);
    }

    @Override
    public void deleteStudentCourseByName(String courseName) throws Exception {
        MyUpdate.update("DELETE FROM `student`.`student_courses` WHERE courseName = ?", courseName);
    }

    @Override
    public void insertStudentCourseAfterDropCourse(Courses c) throws Exception {
        MyUpdate.update("INSERT INTO `student`.`student_courses` (`key`" +
                        ", `score`, `information`, `courseName`) VALUES (?, ?, ?, ?)",
                c.getKey(), c.getScore(), c.getInformation(), c.getCourseName());
    }


}
