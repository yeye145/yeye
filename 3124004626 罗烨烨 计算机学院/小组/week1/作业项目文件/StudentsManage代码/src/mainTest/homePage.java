package mainTest;


import login.Login;

import regist.Register;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import services.menu;
import services.update;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;

import java.util.Properties;
import java.util.Scanner;


public class homePage {

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
            menu.login();

            String choice = sc.next();

            switch (choice) {

                case LOGIN -> new Login(connection);

                case REGISTER -> new Register(connection);

                case FORGET_PASSWORD -> update.forgetPassword(connection, sc);

                case EXIT -> {
                    connection.close();
                    System.exit(0);
                }
                default -> System.out.println("请输入1~3");

            }
        }

    }


}