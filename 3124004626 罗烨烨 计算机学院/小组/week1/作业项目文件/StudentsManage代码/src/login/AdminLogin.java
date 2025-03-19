package login;

import services.*;

import java.sql.Connection;

import java.util.Scanner;


public class AdminLogin {

    //设置常量
    final String SEARCH_ALL_STUDENT = "1";
    final String UPDATE_STUDENT_PHONE_NUMBER = "2";
    final String SEARCH_ALL_COURSE = "3";
    final String UPDATE_COURSE_SCORE = "4";
    final String SEARCH_COURSE_WHO_CHOOSE = "5";
    final String SEARCH_ONE_CHOOSE_WHICH = "6";
    final String INSERT_COURSE = "7";
    final String DELETE_COURSE = "8";
    final String EXIT = "9";

    Scanner sc = new Scanner(System.in);


    public AdminLogin(Connection connection) throws Exception {


        while (true) {

            //打印菜单
            menu.admin();

            //从键盘获取选择
            String choice = sc.next();

            switch (choice) {


                case SEARCH_ALL_STUDENT -> adminSearch.searchAllStudent(connection);

                case UPDATE_STUDENT_PHONE_NUMBER -> update.updatePhoneByAdmin(connection);

                case SEARCH_ALL_COURSE -> adminSearch.searchAllCourse(connection);

                case UPDATE_COURSE_SCORE -> update.updateScore(connection);

                case SEARCH_COURSE_WHO_CHOOSE -> adminSearch.searchCourseStudent(connection);

                case SEARCH_ONE_CHOOSE_WHICH -> adminSearch.searchOneChoose(connection);

                case INSERT_COURSE -> insert.insertCourse(connection);

                case DELETE_COURSE -> delete.deleteCourse(connection);

                case EXIT -> System.exit(0);

                default -> System.out.println("您的输入有误");


            }
        }

    }


}
