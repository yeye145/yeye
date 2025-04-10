package service;

import pojo.Students;

import java.sql.SQLException;

public interface DeleteService {
    void deleteTheseCourses(String dc);

    String dropThisCourse(Students targetStudent, Integer id) throws Exception;
}
