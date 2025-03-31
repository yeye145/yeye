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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        try {

            for (Users user : users) {
                if (user.getPhoneNumber().equals(phone) || user.getEmail().equals(email)) {
                    // 用户已存在
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"用户已存在\"}");
                    return;
                }
            }

            Connection connection = MyPool.getConnection();

            MyUpdate.update("INSERT INTO `student`.`users` (`phoneNumber`, `password`," +
                    " `isAdmin`, `email`) VALUES (?, ?, 1, ?);",connection, phone, password, email);

            connection.close();

            response.getWriter().write("{\"success\":true, \"message\":\"注册成功\"}");
            System.out.println("注册成功！");

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
