package web;

import javabean.people.Users;
import myHandWriteTool.MyPool;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private Set<Users> users;


    {
        Connection conn = MyPool.getConnection();
        try {
            this.users = MySearch.searchToSet("SELECT phoneNumber, id, password, isAdmin, email FROM student.users"
                    , conn, Users.class);
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");

        // 获取参数
        String account = request.getParameter("account");
        String password = request.getParameter("newPassword");


        try {

            for (Users user : users) {
                if (user.getPhoneNumber().equals(account) || user.getEmail().equals(account)) {

                    // 找得到该用户
                    Connection connection = MyPool.getConnection();

                    MyUpdate.update("UPDATE `student`.`users` SET `password` = ? WHERE (`id` = ?);"
                            ,connection, password, user.getId());

                    connection.close();
                    response.getWriter().write("{\"success\":true, \"message\":\"修改成功\"}");
                    System.out.println("修改成功！");
                    return;
                }
            }


            // 用户不存在
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"账号不存在\"}");
            System.out.println("账号不存在！");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"服务器内部错误\"}");
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        this.doGet(request, response);

    }



}
