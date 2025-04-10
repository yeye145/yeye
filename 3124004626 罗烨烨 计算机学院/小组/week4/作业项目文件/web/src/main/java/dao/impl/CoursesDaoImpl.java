package dao.impl;

import dao.CoursesDao;
import dao.utils.MySearch;
import dao.utils.MyUpdate;
import pojo.Courses;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class CoursesDaoImpl implements CoursesDao {
    @Override
    public void updateScoreInCourses(String courseName, Integer newScore) throws Exception {
        MyUpdate.update("UPDATE `student`.`courses` SET `score` = ? " +
                "WHERE (`courseName` = ?);", newScore, courseName);
    }

    // 获取新的sql.courses信息，用于操作
    @Override
    public Set<Courses> getCourseSet() throws SQLException {
        return MySearch.searchToSet("SELECT * FROM student.courses", Courses.class);
    }

    // 获取新的课程信息，用于查询打印
    @Override
    public List<Courses> getCourseList() throws SQLException {
        return MySearch.searchToList("SELECT * FROM student.courses", Courses.class);
    }


    @Override
    public void insertNewCourse(Integer score, Integer max, String information, String name) throws Exception {
        String sql = "INSERT INTO `student`.`courses` " +

                "(`courseName`, `score`," +

                " `information`, `ifCanChoose`, " +

                "`numberCanChoose`, `numberChoose`)" +

                " VALUES (?, ?, ?, '1', ?, '0')";
        //更新课程表
        MyUpdate.update(sql, name, score, information, max);
    }


    @Override
    public void deleteCourseByName(String courseName) throws Exception {
        MyUpdate.update("DELETE FROM `student`.`courses` WHERE courseName = ?", courseName);
    }

    // 选课，选课总人数+1
    @Override
    public void updateCourseAfterSelectCourse(Courses courses) throws Exception {
        //更新课程表信息
        MyUpdate.update("UPDATE student.courses SET numberChoose = ? WHERE `key` = ?"
                , courses.getNumberChoose() + 1, courses.getKey());

    }

    @Override
    public void turnCourseNotSelect(Integer key) throws Exception {
        //将 课程表 中，该课程设置为不可选
        MyUpdate.update("UPDATE student.courses SET ifCanChoose = 0 WHERE `key` = ?", key);
    }

    @Override
    public void turnCourseCanSelect(Integer key) throws Exception {
        //将 课程表 中，该课程设置为不可选
        MyUpdate.update("UPDATE student.courses SET ifCanChoose = 1 WHERE `key` = ?", key);
    }

    // 退课，选课总人数-1
    @Override
    public void updateCourseAfterDropCourse(Courses courses) throws Exception {
        MyUpdate.update("UPDATE student.courses SET numberChoose = ? WHERE `key` = ?",
                courses.getNumberChoose() - 1, courses.getKey());

    }





}
