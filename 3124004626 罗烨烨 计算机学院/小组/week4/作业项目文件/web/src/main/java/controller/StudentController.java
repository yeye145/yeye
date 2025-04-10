package controller;

import com.alibaba.fastjson.JSON;

import pojo.Students;
import pojo.Users;
import service.*;
import service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/student/*")
public class StudentController extends MyBaseServlet {

    // 通过接口调用实现类（面向接口编程）
    private SearchService searchService = new SearchServiceImpl();
    private InsertService insertService = new InsertServiceImpl();
    private DeleteService deleteService = new DeleteServiceImpl();


    // 查看可选课程
    public void scLook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        response.getWriter().write(searchService.studentSearchAllStudentCourses());
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

        Students targetStudent = searchService.searchOneStudentByEmail(u.getEmail());

        if (targetStudent == null) {
            response.getWriter().write("{\"code\":401, \"error\":\"用户不存在！\"}");
            return;
        }

        String json = JSON.toJSONString(targetStudent);

        response.getWriter().write("{\"code\":200,\"data\":" + json + "}");
    }



    // 获取个人选课信息
    public void lookMyCourse(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");
        Students targetStudent = searchService.searchOneStudentByEmail(u.getEmail());
        if (targetStudent.getClassNumber() == 0) return;
        response.getWriter().write(searchService.studentLookHisCourse(targetStudent));
    }


    // 学生选课
    public void selectThisCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");
        Students targetStudent = searchService.searchOneStudentByEmail(u.getEmail());

        if (targetStudent.getClassNumber() == 5) {
            response.getWriter().write("{\"code\":401, \"message\":\"最多选5门课程\"}");
            return;
        }

        Integer id = Integer.parseInt(request.getParameter("key"));
        response.getWriter().write(insertService.selectNewCourse(id, targetStudent));

    }


    // 学生退课
    public void dropThisCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");
        Students targetStudent = searchService.searchOneStudentByEmail(u.getEmail());
        Integer id = Integer.parseInt(request.getParameter("key"));
        response.getWriter().write(deleteService.dropThisCourse(targetStudent, id));
    }

}
