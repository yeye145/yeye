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



        /*被淘汰的方法
        try {
            while (true) {


                //定义sql
                String sql = "select * from student.users";

                //获取pstmt对象
                PreparedStatement pstmt = connection.prepareStatement(sql);

                ResultSet rs = pstmt.executeQuery();

                Scanner sc = new Scanner(System.in);

                //打印登录界面
                System.out.printf("===== 用户登录 =====\n" + "请输入用户名：\n>");
                String username = sc.next();

                System.out.printf("请输入密码：\n>");
                String password = sc.next();

                while (rs.next()) {

                    if (username.equals(rs.getString("users_name")) &&
                            password.equals(rs.getString("users_password"))) {

                        if (rs.getString("isAdmin").equals("1")) {
                            System.out.println("登录成功！你的角色是：学生");
                            new StudentLogin(connection);
                        }

                        if (rs.getString("isAdmin").equals("2")) {
                            System.out.println("登录成功！你的角色是：管理员");
                            new AdminLogin(connection);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

    }


}
