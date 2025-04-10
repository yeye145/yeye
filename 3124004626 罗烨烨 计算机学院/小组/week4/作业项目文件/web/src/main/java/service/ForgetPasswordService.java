package service;

import java.sql.SQLException;

public interface ForgetPasswordService {
    String forgetPassword(String account, String password) throws Exception;
}
