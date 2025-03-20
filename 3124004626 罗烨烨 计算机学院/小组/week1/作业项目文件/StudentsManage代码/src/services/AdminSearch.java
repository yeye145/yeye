package services;

import javabean.course.Courses;
import javabean.people.Students;
import myHandWriteTool.MySearch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public class AdminSearch {


    /*
     *
     *
     * 查询某个学生选课情况
     *
     * */
    public static void searchOneChoose(Connection connection) throws Exception {

        Scanner sc = new Scanner(System.in);

        //将学生表信息封装到HashSet中
        Set<Students> studentSet = MySearch.searchToSet("SELECT students_id `id`, students_name `name`" +
                ", students_phoneNumber `phoneNumber`, students_classHadSelected `classHadSelected`" +
                "FROM student.students WHERE students_classNumber>=0", connection, Students.class);


        //将课程表信息封装到HashSet中
        Set<Courses> courseSet = MySearch.searchToSet("SELECT courses_key `key`," +
                " courses_name `courseName`, courses_score `score`, courses_information `information`" +
                "FROM student.courses", connection, Courses.class);


        while (true) {

            System.out.println("请输入学生的学号");
            String id = sc.next();

            //遍历Set找到该学生
            for (Students s : studentSet) {

                //如果找到了
                if (s.getId() == Integer.parseInt(id)) {

                    System.out.println("找到学号为 " + id + "的学生（仅显示部分课程信息）");
                    System.out.println("姓名：" + s.getName());
                    System.out.println("电话：" + s.getPhoneNumber());

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
                    return;
                }
            }

            System.out.println("找不到该学生");
        }
    }




    /*
     *
     *
     * SEARCH_COURSE_WHO_CHOOSE -> searchCourseStudent(connection);
     * 👇搜索某门课程的学生
     *
     *
     * */


    public static void searchCourseStudent(Connection connection) throws Exception {

        Scanner sc = new Scanner(System.in);

        Set<Students> set = MySearch.searchToSet("SELECT students_id `id`, students_name `name`" +
                ", students_phoneNumber `phoneNumber`, students_classHadSelected `classHadSelected`" +
                ",students_idNumber `idNumber`, students_birthday `birthday`, students_gender `gender`" +
                "FROM student.students WHERE students_classNumber>=0", connection, Students.class);

        boolean exist = false;

        while (true) {

            System.out.print("请输入课程名称:\n>");
            String name = sc.next();

            for (Students s : set) {
                if (s.getClassHadSelected().contains(name)) {
                    if (!exist) {
                        System.out.println("找到符合要求的学生");
                    }
                    exist = true;
                    s.setClassHadSelected(name);
                    System.out.println(s);
                }
            }
            if (exist) return;
            System.out.println("查询不到该课程");
        }
    }




    /*
     *
     *
     *👇 查找所有课程信息
     *
     * */


    public static void searchAllCourse(Connection connection) throws SQLException {

        MySearch.searchToList("SELECT courses_key `key`, courses_name `courseName`" +
                ", courses_score `score`, courses_information `information`" +
                ",ifCanChoose , courses_numberCanChoose `numberCanChoose`, courses_numberChoose `numberChoose`" +
                "FROM student.courses", connection, Courses.class).forEach(System.out::println);


    }




    /*
     *
     *
     *👇 搜索所有学生信息
     *
     * */


    public static void searchAllStudent(Connection connection) throws SQLException {

        MySearch.searchToList("SELECT students_id `id`, students_name `name`, students_classNumber `classNumber`" +
                ", students_phoneNumber `phoneNumber`, students_classHadSelected `classHadSelected`" +
                ",students_idNumber `idNumber`, students_birthday `birthday`, students_gender `gender`" +
                "FROM student.students WHERE students_classNumber>=0", connection, Students.class).forEach(System.out::println);

    }


}
