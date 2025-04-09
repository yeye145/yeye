package dao.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySearch {


    //结果封装到list集合中
    public static <T> List<T> searchToList(String sql, Class<T> clazz, Object... args) throws SQLException {

        List<T> result = new ArrayList<T>();

        Connection connection = MyPool.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            pstmt = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            //执行sql，获取结果集
            rs = pstmt.executeQuery();

            //获取结果集中的元数据
            ResultSetMetaData metaData = rs.getMetaData();


            while (rs.next()) {
                T t = clazz.newInstance();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    //获取结果集对应值
                    Object value = rs.getObject(metaData.getColumnLabel(i));


                    //利用反射获取属性值
                    Field field = clazz.getDeclaredField(metaData.getColumnLabel(i));

                    //忽略private
                    field.setAccessible(true);

                    //将结果封装
                    field.set(t, value);
                }

                result.add(t);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pstmt.close();
            connection.close();
        }

        return result;

    }

    //结果封装到Set集合中
    public static <T> Set<T> searchToSet(String sql, Class<T> clazz, Object... args) throws SQLException {

        Set<T> result = new HashSet<>();
        Connection connection = MyPool.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            pstmt = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            //执行sql，获取结果集
            rs = pstmt.executeQuery();

            //获取结果集中的元数据
            ResultSetMetaData metaData = rs.getMetaData();


            while (rs.next()) {
                T t = clazz.newInstance();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    //获取结果集对应值
                    Object value = rs.getObject(metaData.getColumnLabel(i));


                    //利用反射获取属性值
                    Field field = clazz.getDeclaredField(metaData.getColumnLabel(i));

                    //忽略private
                    field.setAccessible(true);

                    //将结果封装
                    field.set(t, value);
                }

                result.add(t);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pstmt.close();
            connection.close();
        }

        return result;

    }

    //结果封装到单个？中
    public static <T> T searchToOne(String sql, Class<T> clazz, Object... args) throws SQLException {

        T t = null;
        Connection connection = MyPool.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            pstmt = connection.prepareStatement(sql);

            //填充占位符
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }

            //执行sql，获取结果集
            rs = pstmt.executeQuery();

            //获取结果集中的元数据
            ResultSetMetaData metaData = rs.getMetaData();


            if (rs.next()) {

                t = clazz.newInstance();

                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    //获取结果集对应值
                    Object value = rs.getObject(metaData.getColumnLabel(i));


                    //利用反射获取属性值
                    Field field = clazz.getDeclaredField(metaData.getColumnLabel(i));

                    //忽略private
                    field.setAccessible(true);

                    //将结果封装
                    field.set(t, value);
                }

            }
            if (rs.next()) {
                System.out.println("检测到多个结果，但本方法只能返回一个，若要全部返回，请更换searchToList或searchToSet方法");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pstmt.close();
            connection.close();
        }

        return t;

    }

}
