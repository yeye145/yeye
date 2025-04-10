package service;

import pojo.Students;

public interface InsertService {
    // 增设课程
    void insertNewCourse(Integer score, Integer max, String information, String name) throws Exception;

    String selectNewCourse(Integer id, Students targetStudent) throws Exception;
}
