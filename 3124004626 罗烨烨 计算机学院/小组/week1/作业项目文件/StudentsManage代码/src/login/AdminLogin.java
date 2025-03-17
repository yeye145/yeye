package login;

import javabean.course.Courses;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;
import javabean.people.Students;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

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
            adminMenu();

            //从键盘获取选择
            String choice = sc.next();

            switch (choice) {


                case SEARCH_ALL_STUDENT -> searchAllStudent(connection);

                case UPDATE_STUDENT_PHONE_NUMBER -> updatePhone(connection);

                case SEARCH_ALL_COURSE -> searchAllCourse(connection);

                case UPDATE_COURSE_SCORE -> updateScore(connection);

                case SEARCH_COURSE_WHO_CHOOSE -> searchCourseStudent(connection);

                case SEARCH_ONE_CHOOSE_WHICH -> searchOneChoose(connection);

                case INSERT_COURSE -> insertCourse(connection);

                case DELETE_COURSE -> deleteCourse(connection);

                case EXIT -> System.exit(0);

                default -> System.out.println("您的输入有误");


            }
        }

    }

    private void deleteCourse(Connection connection) throws Exception {

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


    private void insertCourse(Connection connection) throws Exception {


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


    private void searchOneChoose(Connection connection) throws Exception {

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


    private void searchCourseStudent(Connection connection) throws Exception {

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
     * UPDATE_COURSE_SCORE -> updateScore(connection);
     * 👇修改某门课程的学分
     *
     *
     * */


    private void updateScore(Connection connection) throws Exception {

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


    /*
     *
     *
     *👇 查找所有课程信息
     *
     * */


    private void searchAllCourse(Connection connection) throws SQLException {

        MySearch.searchToList("SELECT courses_key `key`, courses_name `courseName`" +
                ", courses_score `score`, courses_information `information`" +
                ",ifCanChoose , courses_numberCanChoose `numberCanChoose`, courses_numberChoose `numberChoose`" +
                "FROM student.courses", connection, Courses.class).forEach(System.out::println);


    }



    /*
     *
     *
     *👇更新手机号码
     *
     *
     * */


    private void updatePhone(Connection connection) throws Exception {

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
     *
     *👇 搜索所有学生信息
     *
     * */


    private static void searchAllStudent(Connection connection) throws SQLException {

        MySearch.searchToList("SELECT students_id `id`, students_name `name`, students_classNumber `classNumber`" +
                ", students_phoneNumber `phoneNumber`, students_classHadSelected `classHadSelected`" +
                ",students_idNumber `idNumber`, students_birthday `birthday`, students_gender `gender`" +
                "FROM student.students WHERE students_classNumber>=0", connection, Students.class).forEach(System.out::println);

    }



    /*
     *
     *
     *👇 菜单
     *
     *
     * */


    private static void adminMenu() {
        System.out.print("===== 管理员菜单 =====\n" +
                "1. 查询所有学生\n" +
                "2. 修改学生手机号\n" +
                "3. 查询所有课程\n" +
                "4. 修改课程学分\n" +
                "5. 查询某课程的学生名单\n" +
                "6. 查询某学生的选课情况\n" +
                "7. 增设课程\n" +
                "8. 删除课程\n" +
                "9. 退出\n" +
                "请选择操作（输入 1-8）：");
    }

}
