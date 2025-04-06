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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet("/student/*")
public class StudentServlet extends MyBaseServlet {


    private Set<Students> student;


    private List<StudentCourses> printStudentCourses;
    private Set<Courses> course;

    private Students targetStudent = null;
    private String phone;

    {
        try {
            // 获得学生sql信息
            this.student = MySearch.searchToSet("SELECT * FROM student.students", Students.class);

            // 获得可选课程sql信息
            this.printStudentCourses = MySearch.searchToList("SELECT * FROM student.student_courses;", StudentCourses.class);

            // 获得全部课程sql信息
            this.course = MySearch.searchToSet("SELECT * FROM student.courses;", Courses.class);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 查看可选课程
    public void scLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonString = JSON.toJSONString(this.printStudentCourses);
        response.getWriter().write(jsonString);
    }


    // 获得个人信息
    public void mLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");

        if (u == null || u.getIsAdmin() == 2) {
            System.out.println("未登录或是管理员");
            response.getWriter().write("{\"code\":401, \"message\":\"" +
                    (u == null ? "未登录" : "您是管理员") + "\"}");
            return;
        }

        phone = u.getPhoneNumber();

        refreshStudent();

        if (targetStudent == null) {
            response.getWriter().write("{\"code\":401, \"error\":\"用户不存在！\"}");
            return;
        }

        String json = JSON.toJSONString(targetStudent);

        response.getWriter().write("{\"code\":200,\"data\":" + json + "}");
    }

    private void refreshStudent() {
        // 查找目标学生信息
        for (Students s : this.student) {
            if (phone.equals(s.getPhoneNumber())) {
                targetStudent = s;
                break;
            }
        }
    }


    // 获取个人选课信息
    public void lookMyCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (targetStudent.getClassNumber() == 0) return;

        refreshStudent();

        //将其 选课情况 根据 + 号 切割，放进数组
        String[] courseArray = targetStudent.getClassHadSelected().split("\\+");

        List<Courses> myCourse = new ArrayList<Courses>();

        //遍历数组，找到对应的课程
        for (int i = 0; i < courseArray.length; i++) {
            for (Courses c : course) {
                if (courseArray[i].equals(c.getCourseName())) {
                    myCourse.add(c);
                }
            }
        }

        String jsonString = JSON.toJSONString(myCourse);
        response.getWriter().write(jsonString);
    }


}
