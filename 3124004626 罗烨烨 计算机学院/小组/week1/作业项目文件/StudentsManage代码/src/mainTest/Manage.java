package mainTest;

import javabean.people.Users;
import login.Login;
import myHandWriteTool.MySearch;
import myHandWriteTool.MyUpdate;
import regist.Register;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class Manage {

    final static String LOGIN = "1";
    final static String REGISTER = "2";
    final static String FORGET_PASSWORD = "3";
    final static String EXIT = "4";

    public static void app() throws Exception {


        //加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("StudentsManage代码/src/druid.properties"));

        //获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //获取数据库连接Connection
        Connection connection = dataSource.getConnection();

        Scanner sc = new Scanner(System.in);

        while (true) {

            //打印登录界面
            loginMenu();

            String choice = sc.next();

            switch (choice) {

                case LOGIN -> new Login(connection);

                case REGISTER -> new Register(connection);

                case FORGET_PASSWORD -> forgetPassword(connection, sc);

                case EXIT -> {
                    connection.close();
                    System.exit(0);
                }
                default -> System.out.println("请输入1~3");

            }
        }

    }

    private static void forgetPassword(Connection connection, Scanner sc) throws Exception {


        Set<Users> users = MySearch.searchToSet("SELECT users_name `name`, users_id `id`," +
                "users_password `password` FROM student.users", connection, Users.class);

        while (true) {

            System.out.print("请输入用户名（输入0以返回）:\n>");
            String name = sc.next();

            if (name.equals("0")) return;

            for (Users u : users) {

                if (u.getName().equals(name)) {
                    System.out.print("请输入新的密码:\n>");
                    String password = sc.next();

                    if (password.length() > 30) {
                        System.out.println("你这密码太长了，修改失败");
                        return;
                    }

                    MyUpdate.update("UPDATE student.users SET users_password = ?" +
                            " WHERE users_id = ?", connection, password, u.getId());

                    System.out.println("修改成功");
                    return;
                }
            }
        }

    }

    /*
     *
     * 登录界面菜单
     *
     * */

    public static void loginMenu() {
        System.out.printf("===========================\n" +
                "学生选课管理系统\n" +
                "===========================\n" +
                "1. 登录\n" +
                "2. 注册\n" +
                "3. 修改密码\n" +
                "4. 退出\n" +
                "请选择操作（输入 1-3）：");
    }

}