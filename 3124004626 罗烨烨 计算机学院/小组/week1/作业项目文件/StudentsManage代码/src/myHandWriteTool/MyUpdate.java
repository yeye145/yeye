package myHandWriteTool;


import com.alibaba.druid.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyUpdate {

    public static int update(String sql, Connection connection, Object... args) throws Exception {

        //更新包括增、删、改
        int row = 0;

        PreparedStatement pstmt = null;

        try {
            //获取pstmt对象
            pstmt = connection.prepareStatement(sql);

            //遍历，填充占位符
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            row = pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pstmt.close();
        }


        //返回更新的行
        return row;
    }
}
