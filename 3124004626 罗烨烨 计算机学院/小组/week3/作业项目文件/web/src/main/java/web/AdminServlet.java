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

@WebServlet("/admin/*")
public class AdminServlet extends MyBaseServlet {

    private Connection connection = MyPool.getConnection();
    // 查询用的集合Set
    private Set<Students> students;
    private Set<Users> users;

    // 打印用的集合Set
    private List<StudentCourses> studentCourses;
    private List<Courses> courses;
    private List<Students> printStudents;

    {
        try {
            // 获得学生sql信息
            this.students = MySearch.searchToSet("SELECT * FROM student.students", this.connection, Students.class);

            // 获得可选课程sql信息
            this.studentCourses = MySearch.searchToList("SELECT * FROM student.student_courses;", this.connection, StudentCourses.class);

            // 获得可选课程sql信息
            this.courses = MySearch.searchToList("SELECT * FROM student.courses;", this.connection, Courses.class);

            // 获得用户sql信息
            this.users = MySearch.searchToSet("SELECT * FROM student.users;", this.connection, Users.class);

            // 获得学生sql信息
            this.printStudents = MySearch.searchToList("SELECT * FROM student.students", this.connection, Students.class);


            // 关闭连接
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // 全部课程
    public void cLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String jsonString = JSON.toJSONString(this.courses);
        System.out.println("全部课程" + jsonString);
        response.getWriter().write(jsonString);

    }

    // 全部课程
    public void sLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String jsonString = JSON.toJSONString(this.printStudents);
        System.out.println("全部学生" + jsonString);
        response.getWriter().write(jsonString);

    }



}
