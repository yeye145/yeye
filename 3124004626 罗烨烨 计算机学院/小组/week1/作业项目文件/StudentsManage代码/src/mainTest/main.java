package mainTest;

import login.Login;
import regist.Register;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.Scanner;

public class main {

    final static String LOGIN = "1";
    final static String REGISTER = "2";
    final static String EXIT= "3";

    public static void main(String[] args) throws Exception {

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

                case REGISTER-> new Register(connection);

                case EXIT -> {
                    connection.close();
                    System.exit(0);
                }
                default -> System.out.println("请输入1~3");

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
                "3. 退出\n" +
                "请选择操作（输入 1-3）：");
    }

}