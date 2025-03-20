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
            Menu.admin();

            //从键盘获取选择
            String choice = sc.next();

            switch (choice) {


                case SEARCH_ALL_STUDENT -> AdminSearch.searchAllStudent(connection);

                case UPDATE_STUDENT_PHONE_NUMBER -> Update.updatePhoneByAdmin(connection);

                case SEARCH_ALL_COURSE -> AdminSearch.searchAllCourse(connection);

                case UPDATE_COURSE_SCORE -> Update.updateScore(connection);

                case SEARCH_COURSE_WHO_CHOOSE -> AdminSearch.searchCourseStudent(connection);

                case SEARCH_ONE_CHOOSE_WHICH -> AdminSearch.searchOneChoose(connection);

                case INSERT_COURSE -> Insert.insertCourse(connection);

                case DELETE_COURSE -> Delete.deleteCourse(connection);

                case EXIT -> {
                    connection.close();
                    System.exit(0);
                }



                default -> System.out.println("您的输入有误");


            }
        }

    }


}
