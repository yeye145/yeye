package service;

import pojo.Courses;
import pojo.Students;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public interface SearchService {

    String adminSearchAllStudents() throws SQLException;

    String adminSearchOneCourseHaveWhoSelectIt(String courseName) throws SQLException, IOException;

    String adminSearchAllCourses() throws SQLException;

    // 学生查找全部可选课程
    String studentSearchAllStudentCourses() throws SQLException;


    // 检查一门课程是否存在
    boolean searchOneCourseIfExistByName(String courseName) throws SQLException;

    // 通过课程信息，检查一门课程是否存在
    boolean searchOneCourseIfExistByInformation(String information) throws SQLException;


    Students searchOneStudentByEmail(String email) throws SQLException;


    String studentLookHisCourse(Students targetStudent) throws SQLException;
}
