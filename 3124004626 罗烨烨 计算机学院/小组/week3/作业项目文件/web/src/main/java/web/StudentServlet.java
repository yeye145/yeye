package web;

import com.alibaba.fastjson.JSON;
import javabean.course.Courses;
import javabean.course.StudentCourses;
import javabean.people.Students;
import javabean.people.Users;
import myHandWriteTool.MyPool;
import myHandWriteTool.MySearch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@WebServlet("/student/*")
public class StudentServlet extends MyBaseServlet {


    private Set<Students> students;
    private Set<Users> users;

    private List<StudentCourses> studentCourses;
    private List<Courses> courses;

    {
        try {
            // 获得学生sql信息
            this.students = MySearch.searchToSet("SELECT * FROM student.students", Students.class);

            // 获得可选课程sql信息
            this.studentCourses = MySearch.searchToList("SELECT * FROM student.student_courses;", StudentCourses.class);

            // 获得可选课程sql信息
            this.courses = MySearch.searchToList("SELECT * FROM student.courses;", Courses.class);

            // 获得可选课程sql信息
            this.users = MySearch.searchToSet("SELECT * FROM student.users;", Users.class);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 查看可选课程
    public void scLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonString = JSON.toJSONString(this.studentCourses);
        System.out.println("可选课程" + jsonString);
        response.getWriter().write(jsonString);
    }
    public void cLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonString = JSON.toJSONString(this.courses);
        System.out.println("全部课程" + jsonString);
        response.getWriter().write(jsonString);
    }

    // 获得个人信息
    public void mLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (OtherServlet.studentAccount.isEmpty()) {
            response.getWriter().write("{\"code\":401, \"error\":\"用户不存在！\"}");
            System.out.println("未登录");
            return;
        }

        System.out.println("电话" + OtherServlet.studentAccount);

        String phone = OtherServlet.studentAccount;


        // 查找目标学生信息
        Students targetStudent = null;
        for (Students s : this.students) {
            if (phone.equals(s.getPhoneNumber())) {
                targetStudent = s;
                break;
            }
        }

        if (targetStudent == null) {
            response.getWriter().write("{\"code\":401, \"error\":\"用户不存在！\"}");
            return;
        }

        String json = JSON.toJSONString(targetStudent);

        System.out.println("\n个人信息" + json);
        response.getWriter().write("{\"code\":200,\"data\":" + json + "}");
    }

}
