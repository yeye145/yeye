package web;

import javabean.people.Students;
import javabean.people.Users;

import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.Set;

@WebServlet("/other/*")
public class OtherServlet extends MyBaseServlet {

    private Set<Users> users;
    private Set<Students> students;


    private String horse = "";

    {
        // 初始化用户
        try {
            initUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        // 初始化用户
        initUsers();

        // 获取参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 设置学生账号为空
        HttpSession session = request.getSession();
        session.setAttribute("user", null);

        try {

            // 遍历集合，找到该用户
            for (Users user : users) {

                if (user.getPhoneNumber().equals(username) || user.getEmail().equals(username)) {
                    // 检验密码是不是正确的
                    checkPassword(response, request, user, password);
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


    private void initUsers() throws SQLException {
        this.users = MySearch.searchToSet("SELECT * FROM student.users", Users.class);
    }


    private void initStudents() throws SQLException {
        this.students = MySearch.searchToSet("SELECT * FROM student.students", Students.class);
    }


    private void checkPassword(HttpServletResponse response, HttpServletRequest request, Users user, String password) throws IOException {
        if (user.getPassword().equals(password)) {
            // 设置用户信息到 session
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // 存储整个用户对象

            // 返回响应
            response.getWriter().write("{\"code\":200, \"message\":\"" +
                    (user.getIsAdmin() == 2 ? "admin" : "student") + "\"}");

            System.out.println("登录成功！" + user.getPhoneNumber());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401, \"error\":\"密码错误\"}");
            System.out.println("密码错误！");
        }
    }


    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        // 初始化用户
        initUsers();
        initStudents();

        // 获取参数
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String idNumber = request.getParameter("idNumber");
        String name = request.getParameter("name");


        try {

            for (Users user : users) {
                if (user.getPhoneNumber().equals(phone) || user.getEmail().equals(email)) {
                    System.out.println("用户已经存在" + user.getPhoneNumber());
                    // 用户已存在
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"用户已存在\"}");
                    return;
                }
            }


            //获取月份和日期
            int year = Integer.parseInt(idNumber.substring(6, 10));
            int moon = Integer.parseInt(idNumber.substring(10, 12));
            int day = Integer.parseInt(idNumber.substring(12, 14));


            String gender = Integer.parseInt(idNumber.substring(16, 17)) % 2 == 0 ? "女" : "男";


            MyUpdate.update("INSERT INTO `student`.`users` (`phoneNumber`, `password`," +
                    " `isAdmin`, `email`) VALUES (?, ?, 1, ?);", phone, password, email);

            MyUpdate.update("INSERT INTO `student`.`students` (`phoneNumber`, `classHadSelected`," +
                            " `classNumber`, `idNumber`, `gender`, `birthday`, `name`, `email`) VALUES " +
                            "(?, ?, 0, ?, ?, ?, ?, ?);", phone, "还没有选课",
                    idNumber, gender, year + "-" + moon + "-" + day, name, email);


            response.getWriter().write("{\"success\":true, \"message\":\"注册成功\"}");
            System.out.println("注册成功！");

        } catch (Exception e) {
            // 返回500
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"服务器内部错误\"}");
            e.printStackTrace();
        }

    }


    public void forget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        // 初始化用户
        initUsers();

        // 获取参数
        String account = request.getParameter("account");
        String password = request.getParameter("newPassword");

        try {

            for (Users user : users) {
                if (user.getPhoneNumber().equals(account) || user.getEmail().equals(account)) {

                    // 找得到该用户

                    MyUpdate.update("UPDATE `student`.`users` SET `password` = ? WHERE (`id` = ?);"
                            , password, user.getId());

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


    public void getHorse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成随机4位验证码
        String verifyCode = generateRandomCode(4);

        this.horse = verifyCode;

        // 存入session
        request.getSession().setAttribute("verifyCode", verifyCode);

        // 创建图片
        int width = 100, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics g = image.getGraphics();

        // 绘制背景
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            g.setColor(getRandomColor(120, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // 绘制验证码
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < verifyCode.length(); i++) {
            g.setColor(new Color(20 + random.nextInt(110),
                    20 + random.nextInt(110),
                    20 + random.nextInt(110)));
            g.drawString(String.valueOf(verifyCode.charAt(i)), 20 * i + 10, 30);
        }

        // 释放图形上下文
        g.dispose();

        // 输出图像到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());

    }


    // 获取随机4位文字验证码
    private String generateRandomCode(int length) {

        // 因为大写的i，小写的L，数字1；数字2和字母Z 容易混淆，所以此处我没加
        String chars = "ABCDEFGHJKMNPQRSTUVWXY3456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        System.out.println(sb);
        return sb.toString();
    }


    // 随机颜色
    private Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }


    // 验证用户输入的验证码
    public void checkHorse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取参数
        String inputHorse = request.getParameter("inputHorse");

        System.out.println("用户输入的验证码是：" + inputHorse);
        // 忽略大小写的比较
        if (inputHorse.equalsIgnoreCase(this.horse)) {
            response.getWriter().write("{\"success\":true, \"message\":\"验证成功\"}");
        } else {
            response.getWriter().write("{\"success\":false, \"message\":\"false\"}");
        }
    }


}




