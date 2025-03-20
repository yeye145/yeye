package services;

import javabean.course.Courses;
import javabean.course.StudentCourses;
import javabean.people.Students;

import myHandWriteTool.MySearch;

import java.sql.Connection;

import java.util.Set;

public class StudentSearch {


    /*
     *
     * 查询自我信息
     *
     * */
    public static void myself(Students s, Set<Courses> courseSet) {


        System.out.println("姓名：" + s.getName());
        System.out.println("学号：" + s.getId());
        System.out.println("电话：" + s.getPhoneNumber());
        System.out.println("生日：" + s.getBirthday());
        System.out.println("性别：" + s.getGender());
        System.out.println("身份证号：" + s.getIdNumber());
        System.out.println("已选课程门数：" + s.getClassNumber());
        System.out.println("已选课程名称：" + s.getClassHadSelected());

        //将其 选课情况 根据 + 号 切割，放进数组
        String[] courseArray = s.getClassHadSelected().split("\\+");

        //遍历数组，找到对应的课程
        for (int i = 0; i < courseArray.length; i++) {

            for (Courses c : courseSet) {

                if (courseArray[i].equals(c.getCourseName())) {
                    System.out.println(c);
                }
            }
        }

    }


    /*
     *
     *
     * 查看可选课程
     *
     * */
    public static void searchCourseCanSelect(Connection connection) throws Exception {
        MySearch.searchToList("SELECT sc_key `key`, sc_name `courseName`, " +
                        "sc_score `score`,sc_information `information` FROM " +
                        "student.student_courses", connection, StudentCourses.class)
                .forEach(System.out::println);
    }


}
