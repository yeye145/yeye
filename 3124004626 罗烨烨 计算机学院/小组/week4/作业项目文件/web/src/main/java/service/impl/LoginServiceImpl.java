package service.impl;

import dao.UsersDao;
import dao.impl.UsersDaoImpl;

import pojo.Users;
import service.LoginService;


import java.sql.SQLException;
import java.util.Set;

public class LoginServiceImpl implements LoginService {

    private UsersDao usersDao = new UsersDaoImpl();


    @Override
    public Users loginCheck(String username, String password) throws SQLException {

        Set<Users> users = usersDao.getUserSet();
        // 遍历集合，找到该用户
        for (Users user : users) {
            if (user.getPhoneNumber().equals(username) || user.getEmail().equals(username)) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        return null;
    }


}
