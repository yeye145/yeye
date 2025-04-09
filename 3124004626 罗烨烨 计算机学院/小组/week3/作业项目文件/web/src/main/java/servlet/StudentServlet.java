package servlet;

import com.alibaba.fastjson.JSON;
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

@WebServlet("/student/*")
public class StudentServlet extends MyBaseServlet {

    private Set<Students> student;

    private List<StudentCourses> printStudentCourses;
    private Set<Courses> course;

    private Students targetStudent = null;
    private String email;

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
    public void scLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        this.printStudentCourses = MySearch.searchToList("SELECT * FROM student.student_courses;", StudentCourses.class);
        String jsonString = JSON.toJSONString(this.printStudentCourses);
        response.getWriter().write(jsonString);
    }


    // 获得个人信息
    public void mLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");

        if (u == null || u.getIsAdmin() == 2) {
            System.out.println("未登录或是管理员");
            response.getWriter().write("{\"code\":401, \"message\":\"" +
                    (u == null ? "未登录" : "您是管理员") + "\"}");
            return;
        }

        email = u.getEmail();

        refreshStudent();

        if (targetStudent == null) {
            response.getWriter().write("{\"code\":401, \"error\":\"用户不存在！\"}");
            return;
        }

        String json = JSON.toJSONString(targetStudent);

        response.getWriter().write("{\"code\":200,\"data\":" + json + "}");
    }


    private void refreshStudent() throws SQLException {
        this.student = MySearch.searchToSet("SELECT * FROM student.students", Students.class);
        // 查找目标学生信息
        for (Students s : this.student) {
            if (email.equals(s.getEmail())) {
                targetStudent = s;
                break;
            }
        }
    }


    // 获取个人选课信息
    public void lookMyCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

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


    // 学生选课
    public void selectThisCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        refreshStudent();

        if (targetStudent.getClassNumber() == 5) {
            response.getWriter().write("{\"code\":401, \"message\":\"最多选5门课程\"}");
            return;
        }

        this.course = MySearch.searchToSet("SELECT * FROM student.courses;", Courses.class);

        Integer id = Integer.parseInt(request.getParameter("key"));

        for (Courses c : course) {

            //找到该序号课程
            if (c.getKey() == id && c.getIfCanChoose() == 1) {

                if (targetStudent.getClassHadSelected().contains(c.getCourseName())) {
                    response.getWriter().write("{\"code\":401, \"message\":\"已选该课程\"}");
                    return;
                }

                //更新学生选课信息
                MyUpdate.update("UPDATE student.students SET classHadSelected = ?, classNumber = ? WHERE id = ?",
                        targetStudent.getClassHadSelected() + (targetStudent.getClassHadSelected().isEmpty() ? "" : "+")
                                + c.getCourseName(), targetStudent.getClassNumber() + 1, targetStudent.getId());


                //更新课程表信息
                MyUpdate.update("UPDATE student.courses SET numberChoose = ? WHERE `key` = ?"
                        , c.getNumberChoose() + 1, c.getKey());

                //如果选课人数满了
                if (c.getNumberChoose() + 1 == c.getNumberCanChoose()) {

                    //将 课程表 中，该课程设置为不可选
                    MyUpdate.update("UPDATE student.courses SET ifCanChoose = 0 WHERE `key` = ?", c.getKey());

                    //删除 选课表 中，该课程的信息
                    MyUpdate.update("DELETE FROM `student`.`student_courses` WHERE `key` = ?", c.getKey());

                }
                refreshStudent();
                response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");
                return;
            }

        }

    }


    // 学生退课
    public void dropThisCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        refreshStudent();
        Integer id = Integer.parseInt(request.getParameter("key"));
        this.course = MySearch.searchToSet("SELECT * FROM student.courses;", Courses.class);

        for (Courses c : course) {

            if (c.getKey() == id) {
                String[] drop = targetStudent.getClassHadSelected().split("\\+");
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
                    if (!drop[i].isEmpty()) {
                        if (drop[0].isEmpty() && i == 1) {
                            classInformation += drop[i];
                            continue;
                        }
                        classInformation += "+" + drop[i];
                    }
                }

                //更新学生表信息
                MyUpdate.update("UPDATE student.students SET classHadSelected = ?, classNumber = ? WHERE id = ?",
                        classInformation, targetStudent.getClassNumber() - 1, targetStudent.getId());

                //如果不超过可选人数
                if (c.getIfCanChoose() == 1) {
                    //更新课程表信息
                    MyUpdate.update("UPDATE student.courses SET numberChoose = ? WHERE `key` = ?", c.getNumberChoose() - 1, c.getKey());
                    break;
                }
                //如果一开始已经 达到 最大可选人数
                if (c.getIfCanChoose() == 0) {

                    //更新课程表信息
                    MyUpdate.update("UPDATE student.courses SET numberChoose = ?," +
                            "ifCanChoose = 1 WHERE `key` = ?", c.getNumberChoose() - 1, c.getKey());


                    MyUpdate.update("INSERT INTO `student`.`student_courses` (`key`" +
                                    ", `score`, `information`, `courseName`) VALUES (?, ?, ?, ?)",
                            c.getKey(), c.getScore(), c.getInformation(), c.getCourseName());
                    break;
                }
            }
        }
        refreshStudent();
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");
    }

}
