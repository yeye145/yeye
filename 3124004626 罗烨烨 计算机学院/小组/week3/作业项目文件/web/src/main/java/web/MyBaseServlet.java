package web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyBaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String uri = request.getRequestURI();

        int index = uri.lastIndexOf("/");
        String methodName = uri.substring(index + 1);

        Class<? extends MyBaseServlet> clazz = this.getClass();


        try {
            // 获取方法对象
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            // 执行方法
            method.invoke(this, request, response);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }


    }
}
