package services;

import javabean.course.Courses;
import javabean.people.Students;
import javabean.people.Users;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;

public class Update {

    /*
     *
     * 忘记密码
     * 更新密码
     *
     * */

    public static void forgetPassword(Connection connection, Scanner sc) throws Exception {


        Set<Users> users = MySearch.searchToSet("SELECT users_name `name`, users_id `id`," +
                "users_password `password` FROM student.users", connection, Users.class);

        while (true) {

            System.out.print("请输入用户名（输入0以返回）:\n>");
            String name = sc.next();

            if (name.equals("0")) return;

            for (Users u : users) {

                if (u.getName().equals(name)) {
                    System.out.print("请输入新的密码:\n>");
                    String password = sc.next();

                    if (password.length() > 30) {
                        System.out.println("你这密码太长了，修改失败");
                        return;
                    }

                    MyUpdate.update("UPDATE student.users SET users_password = ?" +
                            " WHERE users_id = ?", connection, password, u.getId());

                    System.out.println("修改成功");
                    return;
                }
            }
        }

    }


    /*
     *
     * 学生登录
     * 学生修改自己的手机号码
     *
     *
     * */

    public static void updatePhoneByStudent(Connection connection, Integer users_key) throws Exception {

        Scanner sc = new Scanner(System.in);

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


    /*
     *
     * 管理员
     *👇更新学生手机号码
     *
     *
     * */


    public static void updatePhoneByAdmin(Connection connection) throws Exception {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.print("请输入学生的原手机号:\n>");
            String searchPhoneNumber = sc.next();
            //如果输入匹配正则表达式

            if (!searchPhoneNumber.matches("1[3-9][0-9]{9}")) {
                System.out.println("您输入的手机号格式有误");
                continue;
            }

            //从 学生表 获取 电话号码 到 HashSet集合
            Set<Students> set = MySearch.searchToSet("SELECT students_id `id`, students_name `name`," +
                    "students_phoneNumber `phoneNumber` " +
                    "FROM student.students WHERE students_classNumber > 0", connection, Students.class);


            //遍历HashSet集合
            for (Students s : set) {


                //如果找得到该电话号码
                if (s.getPhoneNumber().equals(searchPhoneNumber)) {

                    System.out.println("找到学生信息：");
                    System.out.println("学号：" + s.getId());
                    System.out.println("姓名：" + s.getName());
                    System.out.println("手机号：" + s.getPhoneNumber());


                    //进入电话号码修改
                    while (true) {
                        System.out.print("请输入修改后的手机号(输入0以取消修改):\n>");
                        String phoneNew = sc.next();

                        if (phoneNew.equals("0")) return;

                        if (!phoneNew.matches("1[3-9][0-9]{9}")) {
                            System.out.println("您输入的手机号不符合格式");
                            continue;
                        }

                        String sql = "UPDATE student.students SET students_phoneNumber = ? WHERE students_phoneNumber = ?";

                        //调用工具类进行更新
                        MyUpdate.update(sql, connection, phoneNew, searchPhoneNumber);

                        System.out.println("更新成功");
                        return;
                    }
                }
            }

            System.out.println("找不到该手机号");

        }

    }


    /*
     *
     * 管理员
     * UPDATE_COURSE_SCORE -> updateScore(connection);
     * 👇修改某门课程的学分
     *
     *
     * */


    public static void updateScore(Connection connection) throws Exception {

        Scanner sc = new Scanner(System.in);

        //获取课程信息到HahSet集合中
        Set<Courses> courses = MySearch.searchToSet("SELECT courses_key `key`, courses_name `courseName`, " +
                        "courses_information `information`,courses_score `score` FROM student.courses",
                connection, Courses.class);

        while (true) {

            System.out.print("请输入课程的名称:\n>");
            String name = sc.next();

            System.out.print("请输入课程的序号:\n>");
            String key = sc.next();

            if (!key.matches("\\d+")) {
                System.out.println("课程序号应为纯数字");
                continue;
            }

            //遍历课程表
            for (Courses course : courses) {

                if (course.getKey() == Integer.parseInt(key) && course.getCourseName().equals(name)) {
                    System.out.println("找到相关课程信息");
                    System.out.println("课程序号:" + course.getKey());
                    System.out.println("课程名称:" + course.getCourseName());
                    System.out.println("课程信息:" + course.getInformation());
                    System.out.println("课程学分:" + course.getScore());

                    while (true) {

                        System.out.print("请输入修改后的学分:\n>");
                        String score = sc.next();

                        if (!score.matches("\\d+")) {
                            System.out.println("学分必须为数字且为整数");
                            continue;
                        }

                        String sql = "UPDATE student.courses SET courses_score = '" + score +
                                "' WHERE courses_key = '" + course.getKey() + "'";

                        MyUpdate.update(sql, connection);

                        System.out.println("更新成功");

                        return;
                    }
                }
            }

            System.out.println("找不到该课程");

        }
    }


}
