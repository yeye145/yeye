package service;

import java.sql.SQLException;

public interface RegisterService {
    String register(String phone, String email, String password, String idNumber, String name) throws Exception;
}
