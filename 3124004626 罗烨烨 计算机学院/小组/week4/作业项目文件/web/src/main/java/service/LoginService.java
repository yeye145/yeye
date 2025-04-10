package service;

import pojo.Users;

import java.sql.SQLException;

public interface LoginService {

    Users loginCheck(String username, String password) throws SQLException;
}
