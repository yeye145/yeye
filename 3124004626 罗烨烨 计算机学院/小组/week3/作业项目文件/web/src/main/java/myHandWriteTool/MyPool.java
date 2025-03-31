package myHandWriteTool;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.ArrayList;

public class MyPool {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306?useSSL=false&useServerPrepStmts=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "yeye145275";
    private static final int MAXCONN = 10;
    private static final int INITCONN = 5;
    private static ArrayList<Connection> pool;

    static {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            //创建pool 的 ArrayList 对象，指定初始容量为 INITCONN = 5
            pool = new ArrayList<>(INITCONN);

            for (int i = 0; i < INITCONN; i++) {

                //获得connection对象
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                pool.add(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connection getConnection() {
        Connection conn = null;
        if (!pool.isEmpty()) {
            conn = pool.remove(0);
        } else if (pool.size() < MAXCONN) {
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

}