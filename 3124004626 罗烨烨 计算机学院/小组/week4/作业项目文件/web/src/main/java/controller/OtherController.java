package controller;

import pojo.Users;

import service.LoginService;
import service.impl.ForgetPasswordServiceImpl;
import service.impl.LoginServiceImpl;
import service.impl.RegisterServiceImpl;
import service.utils.CaptchaUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/other/*")
public class OtherController extends MyBaseServlet {

    private LoginService loginService = new LoginServiceImpl();
    private RegisterServiceImpl registerService = new RegisterServiceImpl();
    private ForgetPasswordServiceImpl forgetPasswordService = new ForgetPasswordServiceImpl();


    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        // 获取参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Users user = loginService.loginCheck(username, password);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        if (user != null) {
            response.getWriter().write("{\"code\":200, \"message\":\"" +
                    (user.getIsAdmin() == 2 ? "admin" : "student") + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401, \"error\":\"账号或密码错误\"}");
        }

    }


    public void exitLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("user", null);
    }


    public void register(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取参数
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String idNumber = request.getParameter("idNumber");
        String name = request.getParameter("name");
        response.getWriter().write(registerService.register(phone, email, password, idNumber, name));
    }


    public void forget(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取参数
        String account = request.getParameter("account");
        String password = request.getParameter("newPassword");
        response.getWriter().write(forgetPasswordService.forgetPassword(account, password));
    }


    // 生成验证码图片
    public void getHorse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码
        CaptchaUtil.CaptchaResult result = CaptchaUtil.generateCaptcha(100, 40);

        // 将验证码文本存入Session
        request.getSession().setAttribute("verifyCode", result.getCode());

        // 返回图片
        response.setContentType("image/jpeg");
        response.getOutputStream().write(result.getImageBytes());
    }


    // 验证用户输入的验证码
    public void checkHorse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String inputHorse = request.getParameter("inputHorse");
        String realCode = (String) request.getSession().getAttribute("verifyCode");

        boolean isValid = CaptchaUtil.validate(inputHorse, realCode);
        if (isValid) {
            response.getWriter().write("{\"success\":true, \"message\":\"验证成功\"}");
        } else {
            response.getWriter().write("{\"success\":false, \"message\":\"验证失败\"}");
        }
    }

}




