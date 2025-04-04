package web;

import com.alibaba.fastjson.JSON;
import javabean.course.Courses;
import javabean.course.StudentCourses;
import javabean.people.Students;
import javabean.people.Users;

import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

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
        if ( u == null || u.getIsAdmin() !=2) {
            System.out.println("未登录或权限不足");
            response.getWriter().write("{\"code\":401, \"message\":\"" +
                    (u == null ? "未登录" : "权限不足") + "\"}");
            return;
        }

        // 刷新课程信息
        refreshCourses();
        String jsonString = JSON.toJSONString(this.printCourses);
        System.out.println("全部课程" + jsonString);
        response.getWriter().write(jsonString);
    }


    // 查询全部学生
    public void sLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        refreshStudents();
        String jsonString = JSON.toJSONString(this.printStudents);
        System.out.println("全部学生" + jsonString);
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
                System.out.println("全部选" + c.getCourseName() + "的学生" + jsonString);
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

    // 获取新的学生信息，用于查询
    private void refreshStudents() throws SQLException {
        this.printStudents = MySearch.searchToList("SELECT * FROM student.students", Students.class);
    }

    // 获取新的学生信息，用于查询
    private void refreshCourses() throws SQLException {
        this.printCourses = MySearch.searchToList("SELECT * FROM student.courses", Courses.class);
        this.printStudentCourses = MySearch.searchToList("SELECT * FROM student.student_courses", StudentCourses.class);
    }

}
