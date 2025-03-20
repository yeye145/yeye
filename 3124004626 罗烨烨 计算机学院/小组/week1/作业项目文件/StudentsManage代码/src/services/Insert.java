package services;

import javabean.course.Courses;
import javabean.people.Students;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;

public class Insert {


    /*
     *
     * 管理员
     * 增设新课程
     *
     *
     * */
    public static void insertCourse(Connection connection) throws Exception {

        Scanner sc = new Scanner(System.in);

        Set<Courses> courses = MySearch.searchToSet("SELECT courses_key `key`, courses_name `courseName`" +
                ", courses_information `information`" + "FROM student.courses", connection, Courses.class);

        while (true) {


            String name = "";
            String information = "教";
            Integer score = 0;

            //输入课程名称
            while (true) {

                boolean exist = false;

                System.out.print("请输入您要新增的课程名称(输入0以返回)：\n>");
                name = sc.next();

                if (name.equals("0")) return;

                for (Courses c : courses) {
                    if (c.getCourseName().equals(name)) {
                        System.out.println("该课程已存在");
                        exist = true;
                        break;
                    }
                }

                if (!exist) break;
            }


            while (true) {

                //获得地址
                while (true) {

                    System.out.print("请按照以下格式输入该课程的上课地点(输入0以返回)\n" +
                            "例子：若在教3-101教室，您应该输入3-101\n>");

                    String address = sc.next();

                    if (address.equals("0")) return;

                    if (address.matches("[1-6]-[1-6][0-3]\\d")) {
                        information = information + address + "-";
                        break;
                    }

                    System.out.println("格式错误");
                }


                //获得时间
                while (true) {

                    System.out.print("请按照以下格式输入该课程的上课时间(输入0以返回)\n" +
                            "例子：若在周二67节，您应该输入2-67\n>");

                    String time = sc.next();
                    if (time.equals("0")) return;

                    if (time.matches("[1-7]-((12)|(23)|(34)|(45)|(56)|(67)|(78))")) {

                        System.out.println("输入成功");

                        //切割字符串成2，67
                        String[] week = {"0", "一", "二", "三", "四", "五", "六", "日"};
                        String[] timeSplit = time.split("-");

                        //拼接字符串
                        for (int i = 1; i < week.length; i++) {
                            if (i == Integer.parseInt(timeSplit[0])) {
                                information = information + "周" + week[i] + timeSplit[1] + "节";
                                System.out.println(information);
                                System.out.println(information.length());
                                break;
                            }
                        }
                    } else {
                        System.out.println("时间必须往前走");
                    }

                    if (information.length() > 8) break;
                }

                for (Courses c : courses) {
                    if (c.getInformation().equals(information)) {
                        System.out.println("同一个时间同一个地点已经有课程了！");
                        return;
                    }
                }


                while (true) {

                    System.out.print("请输入该课程的学分:\n>");
                    String credit = sc.next();

                    if (!credit.matches("\\d+")) {
                        System.out.println("学分应为数字");
                        continue;
                    }

                    score = Integer.parseInt(credit);
                    break;
                }

                while (true) {

                    System.out.print("请输入该课程的最大选课人数:\n>");
                    String max = sc.next();

                    if (!max.matches("\\d+")) {
                        System.out.println("人数应为正整数");
                        continue;
                    }

                    String sql = "INSERT INTO `student`.`courses` " +

                            "(`courses_name`, `courses_score`," +

                            " `courses_information`, `ifCanChoose`, " +

                            "`courses_numberCanChoose`, `courses_numberChoose`)" +

                            " VALUES (?, ?, ?, '1', ?, '0')";

                    //更新课程表
                    MyUpdate.update(sql, connection, name, score, information, max);

                    System.out.println("添加成功");

                    sql = "INSERT INTO `student`.`student_courses` " +
                            "(`sc_name`, `sc_score`,`sc_information`)" +
                            " VALUES (?, ?, ?)";

                    //更新选课表
                    MyUpdate.update(sql, connection, name, score, information);

                    return;
                }

            }
        }


    }


    /*
     *
     * 学生
     * 选课
     *
     * */
    public static void selectCourse(Connection connection, Set<Courses> courseSet, Students s) throws Exception {

        Scanner sc = new Scanner(System.in);

        String id = "";

        //从键盘获取序号
        while (true) {

            System.out.print("请输入您要选择课程的序号（输入0以返回）:\n>");
            id = sc.next();

            if (id.equals("0")) return;

            if (!id.matches("\\d+")) {
                System.out.println("序号应为正整数");
                continue;
            }

            break;

        }

        for (Courses c : courseSet) {

            //找到该序号课程，并且可选
            if (c.getKey() == Integer.parseInt(id) && c.getIfCanChoose() == 1) {
                System.out.println("找到序号为" + id + "课程");
                System.out.println("课程名称：" + c.getCourseName());
                System.out.println("课程学分：" + c.getScore());
                System.out.println("课程信息：" + c.getInformation());
                System.out.print("请输入yes以选课，输入其他以取消选课:\n>");

                String choice = sc.next();

                if (choice.equalsIgnoreCase("yes")) {

                    String sql = "UPDATE student.students SET students_classHadSelected = ?," +
                            "students_classNumber = ? WHERE students_id = ?";

                    //更新学生选课信息
                    MyUpdate.update(sql, connection, s.getClassHadSelected()
                            + "+" + c.getCourseName(), s.getClassNumber() + 1, s.getId());

                    sql = "UPDATE student.courses SET courses_numberChoose = ? WHERE courses_key = ?";

                    //更新课程表信息
                    MyUpdate.update(sql, connection, c.getNumberChoose() + 1, c.getKey());

                    //如果选课人数满了
                    if (c.getNumberChoose() + 1 == c.getNumberCanChoose()) {

                        //将 课程表 中，该课程设置为不可选
                        sql = "UPDATE student.courses SET ifCanChoose = 0 WHERE courses_key = ?";

                        MyUpdate.update(sql, connection, c.getKey());

                        //删除 选课表 中，该课程的信息
                        sql = "DELETE FROM `student`.`student_courses` WHERE sc_key = ?";

                        MyUpdate.update(sql, connection, c.getKey());

                    }

                } else {
                    System.out.println("取消选课");
                    return;
                }
            }

        }


    }


}
