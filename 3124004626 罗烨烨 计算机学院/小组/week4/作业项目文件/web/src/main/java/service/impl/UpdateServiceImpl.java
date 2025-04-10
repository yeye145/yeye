package service.impl;

import dao.CoursesDao;
import dao.StudentCoursesDao;
import dao.StudentsDao;
import dao.UsersDao;
import dao.impl.CoursesDaoImpl;
import dao.impl.StudentCoursesDaoImpl;
import dao.impl.StudentsDaoImpl;
import dao.impl.UsersDaoImpl;
import service.UpdateService;

public class UpdateServiceImpl implements UpdateService {

    private UsersDao usersDao = new UsersDaoImpl();
    private StudentsDao studentsDao = new StudentsDaoImpl();
    private StudentCoursesDao studentCoursesDao = new StudentCoursesDaoImpl();
    private CoursesDao coursesDao = new CoursesDaoImpl();


    @Override
    public void updatePhone(String newPhone, String email) throws Exception {
        usersDao.updatePhoneInUsers(newPhone,email);
        studentsDao.updatePhoneInStudents(newPhone,email);
        System.out.println("电话号码修改成功！" + newPhone);
    }

    @Override
    public void updateScore(String courseName, Integer newScore) throws Exception {
        studentCoursesDao.updateScoreInStudentCourses(courseName,newScore);
        coursesDao.updateScoreInCourses(courseName,newScore);
        System.out.println("学分修改成功！" + courseName);
    }


}
