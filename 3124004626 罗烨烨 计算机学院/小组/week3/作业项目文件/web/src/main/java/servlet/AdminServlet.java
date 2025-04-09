package servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import pojo.Courses;
import pojo.StudentCourses;
import pojo.Students;
import pojo.Users;

import dao.utils.MySearch;
import dao.utils.MyUpdate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/*")
public class AdminServlet extends MyBaseServlet {

    // 查询用的集合Set
    private Set<Students> student;
    private Set<Users> user;
    private Set<Courses> course;
    private Set<StudentCourses> studentCourse;


    // 打印用的集合List
    private List<StudentCourses> printStudentCourses;
    private List<Courses> printCourses;
    private List<Students> printStudents;


    // 加载sql信息
    {

        try {
            // 获得学生sql信息
            this.student = MySearch.searchToSet("SELECT * FROM student.students", Students.class);

            // 获得可选课程sql信息
            this.printStudentCourses = MySearch.searchToList("SELECT * FROM student.student_courses;", StudentCourses.class);

            // 获得可选课程sql信息
            this.printCourses = MySearch.searchToList("SELECT * FROM student.courses;", Courses.class);

            // 获得用户sql信息
            this.user = MySearch.searchToSet("SELECT * FROM student.users;", Users.class);

            // 获得学生sql信息
            this.printStudents = MySearch.searchToList("SELECT * FROM student.students", Students.class);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // 查询全部课程
    public void cLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");
        if (u == null || u.getIsAdmin() != 2) {
            System.out.println("未登录或权限不足");
            response.getWriter().write("{\"code\":401, \"message\":\"" +
                    (u == null ? "未登录" : "权限不足") + "\"}");
            return;
        }

        // 刷新课程信息
        refreshCourses();
        String jsonString = JSON.toJSONString(this.printCourses);
        response.getWriter().write(jsonString);
    }


    // 查询全部学生
    public void sLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        refreshStudents();
        String jsonString = JSON.toJSONString(this.printStudents);
        response.getWriter().write(jsonString);
    }


    // 查询 选 某门课程的 学生
    public void whoSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        initStudent();
        refreshStudents();

        // 获取参数
        String cName = request.getParameter("cName");
        List<Students> whoSel = new ArrayList<>();

        System.out.println(cName);

        for (Courses c : this.printCourses) {
            if (c.getCourseName().equals(cName)) {

                for (Students s : this.printStudents) {
                    if (s.getClassHadSelected().contains(cName)) {
                        whoSel.add(s);
                    }
                }
                String jsonString = JSON.toJSONString(whoSel);
                response.getWriter().write(jsonString);
                return;
            }
        }


    }


    // 修改某个学生的手机号码
    public void updatePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String newPhone = request.getParameter("newPhone");
        String email = request.getParameter("email");

        MyUpdate.update("UPDATE `student`.`users` SET `phoneNumber` = ? " +
                "WHERE (`email` = ?);", newPhone, email);
        MyUpdate.update("UPDATE `student`.`students` SET `phoneNumber` = ? " +
                "WHERE (`email` = ?);", newPhone, email);
        refreshStudents();
        System.out.println("电话号码修改成功！" + newPhone);
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");

    }


    // 修改某个课程的学分
    public void updateScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String courseName = request.getParameter("courseName");
        Integer newScore = Integer.parseInt(request.getParameter("newScore"));

        MyUpdate.update("UPDATE `student`.`courses` SET `score` = ? " +
                "WHERE (`courseName` = ?);", newScore, courseName);
        MyUpdate.update("UPDATE `student`.`student_courses` SET `score` = ? " +
                "WHERE (`courseName` = ?);", newScore, courseName);
        System.out.println("学分修改成功！" + courseName);
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");

    }


    // 删除课程
    public void deleteCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        initCourse();

        // 获得html传递过来的数组
        String jsonString = request.getReader().readLine();

        // 解析JSON数据
        JSONObject jsonData = JSON.parseObject(jsonString);

        // 获取解析后的数组，链式语句，可能有一点小长
        jsonData.getJSONArray("deleteTheseCourse")
                .toJavaList(String.class)
                .forEach(dc -> {
                            try {
                                initStudent();
                                for (Students s : student) {

                                    //如果没选这门课，跳过该学生
                                    if (!s.getClassHadSelected().contains(dc)) continue;

                                    String[] drop = s.getClassHadSelected().split("\\+");

                                    //拆散选课信息
                                    for (int i = 0; i < drop.length; i++) {
                                        if (drop[i].equals(dc)) {
                                            drop[i] = "";
                                            break;
                                        }
                                    }

                                    String classInformation = drop[0];

                                    //重新组装选课信息
                                    for (int i = 1; i < drop.length; i++) {

                                        if (!drop[i].isEmpty()) {
                                            if (drop[0].isEmpty() && i == 1) {
                                                classInformation += drop[i];
                                                continue;
                                            }
                                            classInformation += "+" + drop[i];
                                        }
                                    }

                                    //更新学生表信息
                                    MyUpdate.update("UPDATE student.students SET " +
                                                    "classHadSelected = ?, classNumber = ? WHERE id = ?",
                                            classInformation, s.getClassNumber() - 1, s.getId());
                                }
                                MyUpdate.update("DELETE FROM `student`.`student_courses` WHERE courseName = ?", dc);
                                MyUpdate.update("DELETE FROM `student`.`courses` WHERE courseName = ?", dc);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");

    }


    // 增设课程
    public void insertCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 初始化课程信息
        initCourse();

        // 检验课程名是否存在
        String name = request.getParameter("newCourseName");
        for (Courses c : course) {
            if (c.getCourseName().equals(name)) {
                response.getWriter().write("{\"code\":401, \"message\":\"该课程已存在\"}");
                return;
            }
        }

        // 检验同一个时间同一个地点是否有课
        String courseBuilding = request.getParameter("newCourseBuilding");
        String courseFloor = request.getParameter("newCourseFloor");
        String courseRoomNumber = request.getParameter("newCourseRoomNumber");
        String courseWeekday = request.getParameter("newCourseWeekday");
        String courseStart = request.getParameter("newCourseStart");
        String courseEnd = request.getParameter("newCourseEnd");

        String information = "教" + courseBuilding + "-" + courseFloor + courseRoomNumber + "-";
        String[] week = {"0", "一", "二", "三", "四", "五", "六", "日"};
        for (int i = 1; i < week.length; i++) {
            if (i == Integer.parseInt(courseWeekday)) {
                information = information + "周" + week[i];
                break;
            }
        }

        if (courseStart.equals(courseEnd)) {
            information += "第" + courseStart + "节";
        } else {
            information += courseStart + "~" + courseEnd + "节";
        }


        for (Courses c : course) {
            if (c.getInformation().equals(information)) {
                response.getWriter().write("{\"code\":401, \"message\":\"同一个时间同一个地点已经有课程了！\"}");
                return;
            }
        }


        Integer score = Integer.parseInt(request.getParameter("newCourseScore"));
        Integer max = Integer.parseInt(request.getParameter("newCourseMaxStudents"));

        String sql = "INSERT INTO `student`.`courses` " +

                "(`courseName`, `score`," +

                " `information`, `ifCanChoose`, " +

                "`numberCanChoose`, `numberChoose`)" +

                " VALUES (?, ?, ?, '1', ?, '0')";
        //更新课程表
        MyUpdate.update(sql, name, score, information, max);


        sql = "INSERT INTO `student`.`student_courses` " +
                "(`courseName`, `score`,`information`)" +
                " VALUES (?, ?, ?)";

        //更新选课表
        MyUpdate.update(sql, name, score, information);

        response.getWriter().write("{\"code\":200, \"message\":\"增设成功\"}");

    }


    // 获取新的sql.users信息，用于操作
    private void initUser() throws SQLException {
        this.user = MySearch.searchToSet("SELECT * FROM student.users", Users.class);
    }

    // 获取新的sql.students信息，用于操作
    private void initStudent() throws SQLException {
        this.student = MySearch.searchToSet("SELECT * FROM student.students", Students.class);
    }

    // 获取新的sql.courses和student_courses信息，用于操作
    private void initCourse() throws SQLException {
        this.course = MySearch.searchToSet("SELECT * FROM student.courses", Courses.class);
        this.studentCourse = MySearch.searchToSet("SELECT * FROM student.student_courses", StudentCourses.class);
    }

    // 获取新的学生信息，用于查询打印
    private void refreshStudents() throws SQLException {
        this.printStudents = MySearch.searchToList("SELECT * FROM student.students", Students.class);
    }

    // 获取新的课程信息，用于查询打印
    private void refreshCourses() throws SQLException {
        this.printCourses = MySearch.searchToList("SELECT * FROM student.courses", Courses.class);
        this.printStudentCourses = MySearch.searchToList("SELECT * FROM student.student_courses", StudentCourses.class);
    }

}
