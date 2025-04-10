package service;

public interface UpdateService{
    void updatePhone(String newPhone, String email) throws Exception;

    void updateScore(String courseName, Integer newScore) throws Exception;
}
