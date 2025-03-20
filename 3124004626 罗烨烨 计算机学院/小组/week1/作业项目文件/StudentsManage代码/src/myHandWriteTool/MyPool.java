package myHandWriteTool;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.ArrayList;

public class MyPool {

    private static String url = "jdbc:mysql://127.0.0.1:3306?useSSL=false&useServerPrepStmts=true";
    private static String username = "root";
    private static String password = "yeye145275";
    private static int maxConn = 10;
    private static int initConn = 5;
    private static ArrayList<Connection> pool;

    static {
        try {

            Class.forName("com.mysql.jdbc.Driver");

            //创建pool 的 ArrayList 对象，指定初始容量为 initConn = 5
            pool = new ArrayList<>(initConn);

            for (int i = 0; i < initConn; i++) {

                //获得connection对象
                Connection conn = DriverManager.getConnection(url, username, password);
                pool.add(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getConnection() {
        Connection conn = null;
        if (pool.size() > 0) {
            conn = pool.remove(0);
        } else if (pool.size() < maxConn) {
            try {
                conn = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

}