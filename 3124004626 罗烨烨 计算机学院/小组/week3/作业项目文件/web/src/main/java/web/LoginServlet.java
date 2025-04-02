package web;

import javabean.people.Users;
import myHandWriteTool.MyPool;
import myHandWriteTool.MySearch;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private Set<Users> users;

    private Connection connection = MyPool.getConnection();

    public static String studentAccount = "";

    {
        try {
            this.users = MySearch.searchToSet("SELECT phoneNumber, id, password, isAdmin, email FROM student.users"
                    , this.connection, Users.class);
        } catch (SQLException e) {
            try {
                // 关闭
                this.connection.close();
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 设置学生账号为空
        studentAccount = "";

        try {

            // 遍历集合，找到该用户
            for (Users user : users) {

                if (user.getPhoneNumber().equals(username) || user.getEmail().equals(username)) {
                    // 检验密码是不是正确的
                    checkPassword(response, user, password);
                    return;
                }
            }

            // 401 - 用户不存在
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

    private void checkPassword(HttpServletResponse response, Users user, String password) throws IOException {
        if (user.getPassword().equals(password)) {
            this.studentAccount = user.getPhoneNumber();
            response.getWriter().write("{\"code\":200, \"message\":\"登录成功\"}");
            System.out.println("登录成功！" + this.studentAccount);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401, \"error\":\"密码错误\"}");
            System.out.println("密码错误！");
        }
    }


}
