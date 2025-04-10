package dao.impl;

import dao.StudentsDao;
import dao.utils.MySearch;
import dao.utils.MyUpdate;
import pojo.Students;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class StudentsDaoImpl implements StudentsDao {


    @Override
    public void updatePhoneInStudents(String newPhone, String email) throws Exception {
        MyUpdate.update("UPDATE `student`.`students` SET `phoneNumber` = ? " +
                "WHERE (`email` = ?);", newPhone, email);
    }

    // 获取新的sql.students信息，用于操作
    @Override
    public Set<Students> getStudentSet() throws SQLException {
        return MySearch.searchToSet("SELECT * FROM student.students", Students.class);
    }

    // 获取新的学生信息，用于查询打印
    @Override
    public List<Students> getStudentList() throws SQLException {
        return MySearch.searchToList("SELECT * FROM student.students", Students.class);
    }


    @Override
    public Students getStudentByEmail(String email) throws SQLException {
        return MySearch.searchToOne("SELECT * FROM student.students " +
                "WHERE `email` = ? ", Students.class, email);
    }


    @Override
    public void dropCourseInStudents(String classInformation, Integer classNumber, Integer id) throws Exception {
        //更新学生表信息
        MyUpdate.update("UPDATE student.students SET " +
                        "classHadSelected = ?, classNumber = ? WHERE id = ?",
                classInformation, classNumber, id);
    }


    @Override
    public void updateStudentAfterSelectCourse(Students targetStudent, String courseName) throws Exception {
        //更新学生选课信息
        MyUpdate.update("UPDATE student.students SET classHadSelected = ?, classNumber = ? WHERE id = ?",
                targetStudent.getClassHadSelected() + (targetStudent.getClassHadSelected().isEmpty() ? "" : "+")
                        + courseName, targetStudent.getClassNumber() + 1, targetStudent.getId());

    }


    @Override
    public void insertStudents(String phone, String email,
                               String idNumber, int year, int moon,
                               int day, String name, String gender) throws Exception {
        MyUpdate.update("INSERT INTO `student`.`students` (`phoneNumber`, `classHadSelected`," +
                        " `classNumber`, `idNumber`, `gender`, `birthday`, `name`, `email`) VALUES " +
                        "(?, ?, 0, ?, ?, ?, ?, ?);", phone, "",
                idNumber, gender, year + "-" + moon + "-" + day, name, email);

    }

}
