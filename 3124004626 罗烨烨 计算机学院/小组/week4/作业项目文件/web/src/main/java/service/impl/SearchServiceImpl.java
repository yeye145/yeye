package service.impl;

import com.alibaba.fastjson.JSON;
import dao.CoursesDao;
import dao.StudentCoursesDao;
import dao.StudentsDao;
import dao.UsersDao;
import dao.impl.CoursesDaoImpl;
import dao.impl.StudentCoursesDaoImpl;
import dao.impl.StudentsDaoImpl;
import dao.impl.UsersDaoImpl;
import pojo.Courses;
import pojo.Students;

import service.SearchService;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchServiceImpl implements SearchService {


    private StudentsDao studentsDao = new StudentsDaoImpl();
    private StudentCoursesDao studentCoursesDao = new StudentCoursesDaoImpl();
    private CoursesDao coursesDao = new CoursesDaoImpl();


    // 管理员查找全部课程
    @Override
    public String adminSearchAllCourses() throws SQLException {
        return JSON.toJSONString(coursesDao.getCourseList());
    }

    // 管理员查找全部学生
    @Override
    public String adminSearchAllStudents() throws SQLException {
        return JSON.toJSONString(studentsDao.getStudentList());
    }


    @Override
    public String adminSearchOneCourseHaveWhoSelectIt(String courseName) throws SQLException, IOException {
        List<Students> studentsList = studentsDao.getStudentList();
        List<Courses> coursesList = coursesDao.getCourseList();

        List<Students> whoSel = new ArrayList<>();

        for (Courses c : coursesList) {
            if (c.getCourseName().equals(courseName)) {
                for (Students s : studentsList) {
                    if (s.getClassHadSelected().contains(courseName)) {
                        whoSel.add(s);
                    }
                }
                break;
            }
        }
        return JSON.toJSONString(whoSel);
    }


    // 学生查找全部可选课程
    @Override
    public String studentSearchAllStudentCourses() throws SQLException {
        return JSON.toJSONString(studentCoursesDao.getStudentCourseList());
    }

    // 通过课程名称，检查一门课程是否存在
    @Override
    public boolean searchOneCourseIfExistByName(String courseName) throws SQLException {
        Set<Courses> coursesSet = coursesDao.getCourseSet();
        for (Courses c : coursesSet) {
            if (c.getCourseName().equals(courseName)) {
                return true;
            }
        }
        return false;
    }


    // 通过课程信息，检查一门课程是否存在
    @Override
    public boolean searchOneCourseIfExistByInformation(String information) throws SQLException {
        Set<Courses> coursesSet = coursesDao.getCourseSet();
        for (Courses c : coursesSet) {
            if (c.getInformation().equals(information)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public Students searchOneStudentByEmail(String email) throws SQLException {
        return studentsDao.getStudentByEmail(email);
    }


    @Override
    public String studentLookHisCourse(Students targetStudent) throws SQLException {
        //将其 选课情况 根据 + 号 切割，放进数组
        String[] courseArray = targetStudent.getClassHadSelected().split("\\+");

        Set<Courses> course = coursesDao.getCourseSet();
        List<Courses> myCourse = new ArrayList<>();

        //遍历数组，找到对应的课程
        for (int i = 0; i < courseArray.length; i++) {
            for (Courses c : course) {
                if (courseArray[i].equals(c.getCourseName())) {
                    myCourse.add(c);
                }
            }
        }

        return JSON.toJSONString(myCourse);
    }

}



