package login;

import javabean.course.Courses;
import javabean.course.StudentCourses;
import javabean.people.Students;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

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
                    " courses_numberChoose `numberChoose` FROM student.courses", connection, Courses.class);


            studentLoginMenu();

            String choice = sc.next();

            switch (choice) {
                case SEARCH_COURSE_CAN_SELECT -> searchCourseCanSelect(connection);
                case SELECT_COURSE -> selectCourse(connection);
                case DROP_COURSE -> dropCourse(connection, courseSet, s);
                case LOOK_MY_SELECT -> myself(s, courseSet);
                case UPDATE_PHONE -> updatePhone(connection, users_key);
                case EXIT -> System.exit(0);

                default -> System.out.println("请输入数字1~6");

            }

        }

    }

    private void dropCourse(Connection connection, Set<Courses> courseSet, Students s) throws Exception {

        while (true) {

            System.out.print("请输入您要退选课程的序号：\n>");
            String id = sc.next();

            if (!id.matches("\\d+")) {
                System.out.println("序号需为正整数");
                continue;
            }

            for (Courses c : courseSet) {

                if (Integer.parseInt(id) == c.getKey() && s.getClassHadSelected().contains(c.getCourseName())) {
                    System.out.println("课程名称：" + c.getCourseName());
                    System.out.println("学分：" + c.getScore());
                    System.out.print("请输入yes以退选，输入其他以取消退选:\n>");

                    String choice = sc.next();

                    if (choice.equalsIgnoreCase("yes")) {

                        String[] drop = s.getClassHadSelected().split("\\+");

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

                            if (!drop[i].equals("")) {

                                if (drop[0].equals("") && i == 1) {
                                    classInformation += drop[i];
                                    continue;
                                }

                                classInformation += "+" + drop[i];
                            }
                        }

                        String sql = "UPDATE student.students SET students_classHadSelected = ?," +
                                "students_classNumber = ? WHERE students_id = ?";

                        //更新学生表信息
                        MyUpdate.update(sql, connection, classInformation, s.getClassNumber() - 1, s.getId());

                        sql = "UPDATE student.courses SET courses_numberChoose = ? WHERE courses_key = ?";

                        //更新课程表信息
                        MyUpdate.update(sql, connection, c.getNumberChoose() - 1, c.getKey());

                        System.out.println("退选成功");
                        return;

                    } else {
                        return;
                    }
                }
            }

            System.out.println("您没有选这门课");
        }
    }

    private void myself(Students s, Set<Courses> courseSet) {


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

    private void updatePhone(Connection connection, Integer users_key) throws Exception {

        //进入电话号码修改
        while (true) {
            System.out.print("请输入修改后的手机号(输入0以取消修改):\n>");
            String phoneNew = sc.next();

            if (phoneNew.equals("0")) return;

            if (!phoneNew.matches("1[3-9][0-9]{9}")) {
                System.out.println("您输入的手机号不符合格式");
                continue;
            }

            String sql = "UPDATE student.students SET students_phoneNumber = ? WHERE students_id = ?";

            //调用工具类进行更新
            MyUpdate.update(sql, connection, phoneNew, users_key);

            System.out.println("更新成功");

            return;
        }

    }

    private void selectCourse(Connection connection) {
    }

    private void searchCourseCanSelect(Connection connection) throws Exception {
        MySearch.searchToList("SELECT sc_key `key`, sc_name `courseName`, " +
                        "sc_score `score`,sc_information `information` FROM " +
                        "student.student_courses", connection, StudentCourses.class)
                .forEach(System.out::println);
    }

    public void studentLoginMenu() {
        System.out.println("===== 学生菜单 =====\n" +
                "1. 查看可选课程\n" +
                "2. 选择课程\n" +
                "3. 退选课程\n" +
                "4. 查看个人信息\n" +
                "5. 修改手机号\n" +
                "6. 退出\n" +
                "请选择操作（输入 1-6）：");
    }
}
