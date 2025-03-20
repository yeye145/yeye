package login;

import javabean.course.Courses;
import javabean.people.Students;

import myHandWriteTool.MySearch;

import services.Delete;
import services.Menu;
import services.Update;
import services.StudentSearch;
import services.Insert;

import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;

public class StudentLogin {

    Scanner sc = new Scanner(System.in);

    final String SEARCH_COURSE_CAN_SELECT = "1";
    final String SELECT_COURSE = "2";
    final String DROP_COURSE = "3";
    final String LOOK_MY_SELECT = "4";
    final String UPDATE_PHONE = "5";
    final String EXIT = "6";

    public StudentLogin(Connection connection, Integer users_key) throws Exception {


        while (true) {

            /*
             *
             *
             * 因为数据会更新，所以每次循环开始获取一次
             *
             * */

            Students s = MySearch.searchToOne("SELECT students_id `id`, students_name `name`, " +
                    "students_classNumber `classNumber`, students_phoneNumber `phoneNumber`, " +
                    "students_classHadSelected `classHadSelected`, students_idNumber `idNumber`, " +
                    "students_birthday `birthday`, students_gender `gender` FROM student.students " +
                    "WHERE students_id = ?", connection, Students.class, users_key);

            //将课程表信息封装到HashSet中
            Set<Courses> courseSet = MySearch.searchToSet("SELECT courses_key `key`," +
                    " courses_name `courseName`, courses_score `score`, courses_information `information`," +
                    " courses_numberChoose `numberChoose`, ifCanChoose, courses_numberCanChoose `numberCanChoose`" +
                    " FROM student.courses", connection, Courses.class);


            Menu.student();

            String choice = sc.next();

            switch (choice) {

                case SEARCH_COURSE_CAN_SELECT -> StudentSearch.searchCourseCanSelect(connection);

                case SELECT_COURSE -> Insert.selectCourse(connection, courseSet, s);

                case DROP_COURSE -> Delete.dropCourse(connection, courseSet, s);

                case LOOK_MY_SELECT -> StudentSearch.myself(s, courseSet);

                case UPDATE_PHONE -> Update.updatePhoneByStudent(connection, users_key);

                case EXIT -> {
                    connection.close();
                    System.exit(0);
                }

                default -> System.out.println("请输入数字1~6");

            }

        }

    }


}
