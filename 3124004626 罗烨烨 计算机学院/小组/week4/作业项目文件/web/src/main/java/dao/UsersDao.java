package dao;

import pojo.Users;

import java.sql.SQLException;
import java.util.Set;

public interface UsersDao {

    // 更新users表中的电话号码
    void updatePhoneInUsers(String newPhone,String email) throws Exception;

    // 获取新的sql.users信息，用于操作
    Set<Users> getUserSet() throws SQLException;

    void insertUser(String phone, String email, String password) throws Exception;

    void updatePassword(String password, Integer id) throws Exception;
}
