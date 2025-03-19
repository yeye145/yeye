package login;

import myHandWriteTool.MySearch;
import javabean.people.Users;

import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;

public class Login {

    public Login(Connection connection) throws Exception {

        Set<Users> users = MySearch.searchToSet("SELECT users_name `name`, users_id `id`," +
                "users_password `password`, isAdmin FROM student.users", connection, Users.class);

        Scanner sc = new Scanner(System.in);

        while (true) {

            //打印登录界面
            System.out.printf("===== 用户登录 =====\n" + "请输入用户名：\n>");
            String username = sc.next();

            System.out.printf("请输入密码：\n>");
            String password = sc.next();

            for (Users u : users) {

                if (u.getName().equals(username) && u.getPassword().equals(password)) {
                    if (u.getIsAdmin() == 1) {

                        System.out.println("登录成功！您的身份是：学生");

                        new StudentLogin(connection, u.getId());
                    } else {

                        System.out.println("登录成功！您的身份是：管理员");

                        new AdminLogin(connection);

                    }
                }
            }
            System.out.println("账号或密码有误");
        }


    }


}
