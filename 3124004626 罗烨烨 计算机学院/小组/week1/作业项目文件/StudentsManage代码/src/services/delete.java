package services;

import javabean.course.Courses;
import javabean.people.Students;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;

public class delete {

    /*
     * 管理员
     * 删除课程
     *
     * */

    public static void deleteCourse(Connection connection) throws Exception {

        Scanner sc = new Scanner(System.in);

        //将课程表信息封装到HashSet中
        Set<Courses> courseSet = MySearch.searchToSet("SELECT courses_key `key`," +
                " courses_name `courseName`, courses_score `score`, courses_information `information`," +
                " courses_numberChoose `numberChoose`, ifCanChoose, courses_numberCanChoose `numberCanChoose`" +
                " FROM student.courses", connection, Courses.class);

        //将学生信息封装到HashSet中
        Set<Students> studentSet = MySearch.searchToSet("SELECT students_id `id`, students_name `name`, " +
                "students_classNumber `classNumber`, students_phoneNumber `phoneNumber`, " +
                "students_classHadSelected `classHadSelected`, students_idNumber `idNumber`, " +
                "students_birthday `birthday`, students_gender `gender` FROM student.students ", connection, Students.class);

        String id = "";

        //从键盘获取序号
        while (true) {

            System.out.print("请输入您要删除课程的序号（输入0以返回）:\n>");
            id = sc.next();

            if (id.equals("0")) return;

            if (!id.matches("\\d+")) {
                System.out.println("序号应为正整数");
                continue;
            }

            break;

        }


        for (Courses c : courseSet) {


            //找到该序号课程
            if (c.getKey() == Integer.parseInt(id)) {

                System.out.println("找到序号为" + id + "课程");
                System.out.println("课程名称：" + c.getCourseName());
                System.out.println("课程学分：" + c.getScore());
                System.out.println("课程信息：" + c.getInformation());
                System.out.println("课程已选人数" + c.getNumberChoose());
                System.out.print("请输入yes以删除，输入其他以取消删除:\n>");

                String choice = sc.next();

                if (choice.equalsIgnoreCase("yes")) {

                    //遍历学生集合，对每个符合要求的学生执行一次退课操作
                    for (Students s : studentSet) {


                        //如果没选这门课，跳过该学生
                        if (!s.getClassHadSelected().contains(c.getCourseName()))
                            continue;

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


                    }

                    //由于联结，先删除选课表中该课程信息
                    String deleteSql = "DELETE FROM `student`.`student_courses` WHERE sc_key = ?";

                    MyUpdate.update(deleteSql, connection, c.getKey());

                    //再删除课程表该课程信息
                    deleteSql = "DELETE FROM `student`.`courses` WHERE courses_key = ?";

                    MyUpdate.update(deleteSql, connection, c.getKey());

                    System.out.println("删除成功");

                } else {
                    System.out.println("取消删除");
                    return;
                }

            }

        }

    }


    /*
     *
     * 学生
     * 退课
     *
     * */

    public static void dropCourse(Connection connection, Set<Courses> courseSet, Students s) throws Exception {

        Scanner sc = new Scanner(System.in);

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


                        //如果不超过可选人数
                        if (c.getIfCanChoose() == 1) {

                            sql = "UPDATE student.courses SET courses_numberChoose = ? WHERE courses_key = ?";

                            //更新课程表信息
                            MyUpdate.update(sql, connection, c.getNumberChoose() - 1, c.getKey());

                        }


                        //如果一开始已经 达到 最大可选人数
                        if (c.getIfCanChoose() == 0) {

                            sql = "UPDATE student.courses SET courses_numberChoose = ?,ifCanChoose = 1 WHERE courses_key = ?";

                            //更新课程表信息
                            MyUpdate.update(sql, connection, c.getNumberChoose() - 1, c.getKey());

                            sql = "INSERT INTO `student`.`student_courses` (`sc_key`, `sc_score`, `sc_information`, `sc_name`)" +
                                    "VALUES (?, ?, ?, ?)";

                            MyUpdate.update(sql, connection, c.getKey(), c.getScore(), c.getInformation(), c.getCourseName());

                        }


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


}
