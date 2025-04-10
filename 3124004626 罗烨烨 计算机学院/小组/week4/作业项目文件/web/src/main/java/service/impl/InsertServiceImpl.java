package service.impl;

import dao.CoursesDao;
import dao.StudentCoursesDao;
import dao.StudentsDao;
import dao.impl.CoursesDaoImpl;
import dao.impl.StudentCoursesDaoImpl;
import dao.impl.StudentsDaoImpl;
import pojo.Courses;
import pojo.Students;
import service.InsertService;

import java.util.Set;

public class InsertServiceImpl implements InsertService {

    private StudentsDao studentsDao = new StudentsDaoImpl();
    private StudentCoursesDao studentCoursesDao = new StudentCoursesDaoImpl();
    private CoursesDao coursesDao = new CoursesDaoImpl();


    // 增设课程
    @Override
    public void insertNewCourse(Integer score, Integer max, String information, String name) throws Exception {
        coursesDao.insertNewCourse(score, max, information, name);
        studentCoursesDao.insertNewStudentCourse(score, max, information, name);
    }


    @Override
    public String selectNewCourse(Integer id, Students targetStudent) throws Exception {

        Set<Courses> course = coursesDao.getCourseSet();

        for (Courses c : course) {

            //找到该序号课程
            if (c.getKey() == id && c.getIfCanChoose() == 1) {

                if (targetStudent.getClassHadSelected().contains(c.getCourseName())) {
                    return "{\"code\":401, \"message\":\"已选该课程\"}";
                }

                studentsDao.updateStudentAfterSelectCourse(targetStudent, c.getCourseName());

                coursesDao.updateCourseAfterSelectCourse(c);


                //如果选课人数满了
                if (c.getNumberChoose() + 1 == c.getNumberCanChoose()) {
                    studentCoursesDao.deleteStudentCourseByName(c.getCourseName());

                    coursesDao.turnCourseNotSelect(c.getKey());

                }
                return "{\"code\":200, \"message\":\"选课成功\"}";
            }

        }
        return "{\"code\":401, \"message\":\"选课失败\"}";
    }


}
