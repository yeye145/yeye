package service.impl;

import dao.CoursesDao;
import dao.StudentCoursesDao;
import dao.StudentsDao;
import dao.impl.CoursesDaoImpl;
import dao.impl.StudentCoursesDaoImpl;
import dao.impl.StudentsDaoImpl;

import pojo.Courses;
import pojo.Students;
import service.DeleteService;


import java.util.Set;

public class DeleteServiceImpl implements DeleteService {

    private StudentsDao studentsDao = new StudentsDaoImpl();
    private StudentCoursesDao studentCoursesDao = new StudentCoursesDaoImpl();
    private CoursesDao coursesDao = new CoursesDaoImpl();


    @Override
    public void deleteTheseCourses(String dc) {
        try {
            Set<Students> student = studentsDao.getStudentSet();
            for (Students s : student) {

                //如果没选这门课，跳过该学生
                if (!s.getClassHadSelected().contains(dc)) continue;

                String[] drop = s.getClassHadSelected().split("\\+");

                //拆散选课信息
                for (int i = 0; i < drop.length; i++) {
                    if (drop[i].equals(dc)) {
                        drop[i] = "";
                        break;
                    }
                }

                String classInformation = drop[0];

                //重新组装选课信息
                for (int i = 1; i < drop.length; i++) {

                    if (!drop[i].isEmpty()) {
                        if (drop[0].isEmpty() && i == 1) {
                            classInformation += drop[i];
                            continue;
                        }
                        classInformation += "+" + drop[i];
                    }
                }

                studentsDao.dropCourseInStudents(classInformation, s.getClassNumber() - 1, s.getId());
            }
            studentCoursesDao.deleteStudentCourseByName(dc);
            coursesDao.deleteCourseByName(dc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String dropThisCourse(Students targetStudent, Integer id) throws Exception {
        Set<Courses> course = coursesDao.getCourseSet();
        for (Courses c : course) {

            if (c.getKey() == id) {
                String[] drop = targetStudent.getClassHadSelected().split("\\+");
                //拆散选课信息
                for (int i = 0; i < drop.length; i++) {
                    if (drop[i].equals(c.getCourseName())) {
                        drop[i] = "";
                        break;
                    }
                }

                String classInformation = drop[0];

                //重新组装选课信息
                for (int i = 1; i < drop.length; i++) {
                    if (!drop[i].isEmpty()) {
                        if (drop[0].isEmpty() && i == 1) {
                            classInformation += drop[i];
                            continue;
                        }
                        classInformation += "+" + drop[i];
                    }
                }

                studentsDao.dropCourseInStudents(classInformation, targetStudent.getClassNumber() - 1, targetStudent.getId());

                //如果不超过可选人数
                if (c.getIfCanChoose() == 1) {
                    coursesDao.updateCourseAfterDropCourse(c);
                    break;
                }
                //如果一开始已经 达到 最大可选人数
                if (c.getIfCanChoose() == 0) {
                    //更新课程表信息
                    coursesDao.updateCourseAfterDropCourse(c);
                    coursesDao.turnCourseCanSelect(c.getKey());
                    studentCoursesDao.insertStudentCourseAfterDropCourse(c);
                    break;
                }
            }
        }
        return "{\"code\":200, \"message\":\"修改成功\"}";
    }

}
