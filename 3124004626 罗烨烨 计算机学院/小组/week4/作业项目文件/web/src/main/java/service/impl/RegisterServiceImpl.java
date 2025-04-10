package service.impl;

import dao.CoursesDao;
import dao.StudentCoursesDao;
import dao.StudentsDao;
import dao.UsersDao;

import dao.impl.StudentsDaoImpl;
import dao.impl.UsersDaoImpl;
import pojo.Users;
import service.RegisterService;

import java.util.Set;

public class RegisterServiceImpl implements RegisterService {

    private UsersDao usersDao = new UsersDaoImpl();
    private StudentsDao studentsDao = new StudentsDaoImpl();


    @Override
    public String register(String phone, String email, String password, String idNumber, String name) throws Exception {


        Set<Users> users = usersDao.getUserSet();

        for (Users user : users) {
            if (user.getPhoneNumber().equals(phone) || user.getEmail().equals(email)) {
                return "{\"error\":\"用户已存在\"}";
            }
        }

        //获取月份和日期
        int year = Integer.parseInt(idNumber.substring(6, 10));
        int moon = Integer.parseInt(idNumber.substring(10, 12));
        int day = Integer.parseInt(idNumber.substring(12, 14));


        String gender = Integer.parseInt(idNumber.substring(16, 17)) % 2 == 0 ? "女" : "男";

        usersDao.insertUser(phone, email, password);
        studentsDao.insertStudents(phone, email, idNumber, year, moon, day, name, gender);

        return "{\"success\":true, \"message\":\"注册成功\"}";

    }

}
