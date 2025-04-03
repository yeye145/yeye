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
import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/*")
public class AdminServlet extends MyBaseServlet {

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
            this.students = MySearch.searchToSet("SELECT * FROM student.students", Students.class);

            // 获得可选课程sql信息
            this.studentCourses = MySearch.searchToList("SELECT * FROM student.student_courses;", StudentCourses.class);

            // 获得可选课程sql信息
            this.courses = MySearch.searchToList("SELECT * FROM student.courses;", Courses.class);

            // 获得用户sql信息
            this.users = MySearch.searchToSet("SELECT * FROM student.users;", Users.class);

            // 获得学生sql信息
            this.printStudents = MySearch.searchToList("SELECT * FROM student.students", Students.class);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // 全部课程
    public void cLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (OtherServlet.studentAccount.isEmpty()) {
            response.getWriter().write("{\"code\":401, \"error\":\"用户不存在！\"}");
            System.out.println("未登录");
            return;
        }
        String jsonString = JSON.toJSONString(this.courses);
        System.out.println("全部课程" + jsonString);
        response.getWriter().write(jsonString);
    }


    // 全部学生
    public void sLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        refreshStudents();
        String jsonString = JSON.toJSONString(this.printStudents);
        System.out.println("全部学生" + jsonString);
        response.getWriter().write(jsonString);
    }


    public void whoSelect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        initStudents();
        refreshStudents();

        // 获取参数
        String cName = request.getParameter("cName");
        List<Students> whoSel = new ArrayList<>();

        System.out.println(cName);

        for (Courses c : this.courses) {
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


    // 修改手机号码
    public void updatePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String newPhone = request.getParameter("newPhone");
        String email = request.getParameter("email");

        initUsers();
        initStudents();

        for (Students s : this.students) {
            if (s.getEmail().equals(email)) {
                MyUpdate.update("UPDATE `student`.`users` SET `phoneNumber` = ? " +
                        "WHERE (`email` = ?);", newPhone, email);
                MyUpdate.update("UPDATE `student`.`students` SET `phoneNumber` = ? " +
                        "WHERE (`email` = ?);", newPhone, email);
                refreshStudents();
                System.out.println("电话号码修改成功！" + newPhone);
                response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");
                return;
            }
        }
        response.getWriter().write("{\"code\":401, \"message\":\"修改失败\"}");
    }



    private void initUsers() throws SQLException {
        this.users = MySearch.searchToSet("SELECT * FROM student.users", Users.class);
    }


    private void initStudents() throws SQLException {
        this.students = MySearch.searchToSet("SELECT * FROM student.students", Students.class);
    }

    private void refreshStudents() throws SQLException {
        this.printStudents = MySearch.searchToList("SELECT * FROM student.students", Students.class);
    }
}
