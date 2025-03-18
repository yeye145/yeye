package regist;

import myHandWriteTool.MySearch;
import javabean.people.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.Set;

public class Register {

    Scanner sc = new Scanner(System.in);

    //编写sql语句
    String sql = "INSERT INTO `student`.`users` " +
            "(`users_name`, `users_password`, `isAdmin`) " +
            "VALUES (?,?,?);";

    public Register(Connection connection) throws Exception {

        Set<Users> user = MySearch.searchToSet("SELECT users_name `name` " +
                "FROM student.users", connection, Users.class);

        //获取pstmt对象
        PreparedStatement pstmt = connection.prepareStatement(sql);


        while (true) {

            //将第一个问号设置为用户名
            System.out.printf("===== 用户注册 =====\n" + "请输入用户名：\n> ");
            String name = sc.next();

            if(name.equals("0")){
                System.out.println("好歹起个非零的用户名吧");
                continue;
            }

            Boolean exist = false;

            if(name.length()>=30){
                System.out.println("用户名不得超过30位");
                continue;
            }

            //检查当前用户名是否已经存在
            for (Users u : user) {
                if (name.equals(u.getName())) {
                    System.out.println("用户名已存在");
                    exist = true;
                    break;
                }
            }

            //如果已经存在，重新创建
            if (exist) continue;

            pstmt.setString(1, name);

            System.out.printf("\n请设置密码：\n> ");
            String key1 = sc.next();

            if(key1.length()>=30){
                System.out.println("密码不得超过30位");
                continue;
            }

            System.out.printf("\n请再次输入密码：\n> ");
            String key2 = sc.next();

            if (key1.equals(key2)) {
                //将第二个问号设置为密码
                pstmt.setString(2, key2);
                break;

            } else {
                System.out.println("您两次输入的密码不一致，请重新输入");
            }

        }

        while (true) {

            System.out.println("请选择角色（输入 1 代表学生，2 代表管理员）：");
            String choice = sc.next();

            if (choice.equals("1") || choice.equals("2")) {

                //将第三个问号设置为1或2
                pstmt.setInt(3, choice.equals("1") ? 1 : 2);
                pstmt.executeUpdate();
                System.out.println("账号注册成功！请完善个人信息");

                new InformationInput(connection, choice);

                System.out.println("注册成功！请返回主界面登录");
                pstmt.close();
                break;

            } else {
                System.out.println("请输入1或2");
            }
        }

    }

}
