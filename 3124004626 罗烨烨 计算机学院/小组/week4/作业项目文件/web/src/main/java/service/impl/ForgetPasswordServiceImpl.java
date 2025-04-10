package service.impl;

import dao.StudentsDao;
import dao.UsersDao;


import dao.impl.UsersDaoImpl;
import pojo.Users;
import service.ForgetPasswordService;

import java.util.Set;

public class ForgetPasswordServiceImpl implements ForgetPasswordService {

    private UsersDao usersDao = new UsersDaoImpl();

    @Override
    public String forgetPassword(String account, String password) throws Exception {
        // 初始化用户
        Set<Users> users = usersDao.getUserSet();

        for (Users user : users) {
            if (user.getPhoneNumber().equals(account) || user.getEmail().equals(account)) {
                // 找得到该用户
                usersDao.updatePassword(password, user.getId());
                return "{\"success\":true, \"message\":\"修改成功\"}";
            }
        }
        // 用户不存在
        return "{\"code\":401, \"error\":\"账号不存在\"}";

    }
}
