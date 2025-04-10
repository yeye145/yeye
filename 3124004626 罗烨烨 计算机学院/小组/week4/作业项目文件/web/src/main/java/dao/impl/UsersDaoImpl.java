package dao.impl;

import dao.UsersDao;
import dao.utils.MySearch;
import dao.utils.MyUpdate;
import pojo.Users;

import java.sql.SQLException;
import java.util.Set;

public class UsersDaoImpl implements UsersDao {

    @Override
    public void updatePhoneInUsers(String newPhone, String email) throws Exception {
        MyUpdate.update("UPDATE `student`.`users` SET `phoneNumber` = ? " +
                "WHERE (`email` = ?);", newPhone, email);
    }

    // 获取新的sql.users信息，用于操作
    @Override
    public Set<Users> getUserSet() throws SQLException {
        return MySearch.searchToSet("SELECT * FROM student.users", Users.class);
    }

    @Override
    public void insertUser(String phone, String email, String password) throws Exception {
        MyUpdate.update("INSERT INTO `student`.`users` (`phoneNumber`, `password`," +
                " `isAdmin`, `email`) VALUES (?, ?, 1, ?);", phone, password, email);

    }

    @Override
    public void updatePassword(String password, Integer id) throws Exception {
        MyUpdate.update("UPDATE `student`.`users` SET `password` = ? WHERE (`id` = ?);"
                , password, id);
    }

}
