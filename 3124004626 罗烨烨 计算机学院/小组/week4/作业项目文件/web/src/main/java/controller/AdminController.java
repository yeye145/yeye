package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import service.DeleteService;
import service.InsertService;
import service.SearchService;
import service.UpdateService;
import service.impl.DeleteServiceImpl;
import service.impl.InsertServiceImpl;
import service.impl.SearchServiceImpl;

import pojo.Users;


import service.impl.UpdateServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.sql.SQLException;

@WebServlet("/admin/*")
public class AdminController extends MyBaseServlet {

    private SearchService searchService = new SearchServiceImpl();
    private UpdateService updateService = new UpdateServiceImpl();
    private InsertService insertService = new InsertServiceImpl();
    private DeleteService deleteService = new DeleteServiceImpl();


    // 查询全部课程
    public void coursesLook(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        HttpSession session = request.getSession();
        Users u = (Users) session.getAttribute("user");
        if (u == null || u.getIsAdmin() != 2) {
            System.out.println("未登录或权限不足");
            response.getWriter().write("{\"code\":401, \"message\":\"" +
                    (u == null ? "未登录" : "权限不足") + "\"}");
            return;
        }
        response.getWriter().write(searchService.adminSearchAllCourses());
    }


    // 查询全部学生
    public void studentsLook(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.getWriter().write(searchService.adminSearchAllStudents());
    }


    // 查询 选 某门课程的 学生
    public void whoSelect(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String cName = request.getParameter("cName");
        response.getWriter().write(searchService.adminSearchOneCourseHaveWhoSelectIt(cName));
    }


    // 修改某个学生的手机号码
    public void updatePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {
        updateService.updatePhone(request.getParameter("newPhone"), request.getParameter("email"));
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");
    }


    // 修改某个课程的学分
    public void updateScore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        updateService.updateScore(request.getParameter("courseName"), Integer.parseInt(request.getParameter("newScore")));
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");
    }


    // 删除课程
    public void deleteCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 获得html传递过来的数组
        String jsonString = request.getReader().readLine();

        // 解析JSON数据
        JSONObject jsonData = JSON.parseObject(jsonString);

        // 获取解析后的数组，链式语句
        jsonData.getJSONArray("deleteTheseCourse")
                .toJavaList(String.class)
                .forEach(deleteService::deleteTheseCourses);
        response.getWriter().write("{\"code\":200, \"message\":\"修改成功\"}");

    }

    // 增设课程
    public void insertCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 检验课程名是否存在
        String name = request.getParameter("newCourseName");

        if (searchService.searchOneCourseIfExistByName(name)) {
            response.getWriter().write("{\"code\":401, \"message\":\"该课程已存在\"}");
            return;
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


        if (searchService.searchOneCourseIfExistByInformation(information)) {
            response.getWriter().write("{\"code\":401, \"message\":\"同一个时间同一个地点已经有课程了！\"}");
            return;
        }


        Integer score = Integer.parseInt(request.getParameter("newCourseScore"));
        Integer max = Integer.parseInt(request.getParameter("newCourseMaxStudents"));

        insertService.insertNewCourse(score, max, information, name);
        response.getWriter().write("{\"code\":200, \"message\":\"增设成功\"}");

    }


}
