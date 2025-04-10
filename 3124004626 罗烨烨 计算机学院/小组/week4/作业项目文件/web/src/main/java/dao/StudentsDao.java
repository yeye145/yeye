package dao;

import pojo.Courses;
import pojo.Students;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface StudentsDao {

    // 更新students表中的电话号码
    void updatePhoneInStudents(String newPhone,String email) throws Exception;

    // 获取新的sql.students信息，用于操作
    Set<Students> getStudentSet() throws SQLException;


    // 获取新的学生信息，用于查询打印
    List<Students> getStudentList() throws SQLException;


    Students getStudentByEmail(String email) throws SQLException;

    void dropCourseInStudents(String classInformation, Integer classNumber, Integer id) throws Exception;

    void updateStudentAfterSelectCourse(Students targetStudent, String courseName) throws Exception;

    void insertStudents(String phone, String email,
                        String idNumber, int year, int moon,
                        int day, String name, String gender) throws Exception;
}
