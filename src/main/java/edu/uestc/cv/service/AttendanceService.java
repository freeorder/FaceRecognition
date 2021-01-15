package edu.uestc.cv.service;

import edu.uestc.cv.exception.ExistException;
import edu.uestc.cv.exception.NotExistException;

import java.util.List;

/**
 * @Author: HQC
 * @Contact: qq545010683
 * @Data: 2020/12/16 23:15
 **/
public interface AttendanceService {
    void addAttendance() throws ExistException, NotExistException;

    void updateAttendance() throws NotExistException;

    List<String> listAllAttendance();

    void deleteAttendance(List<String> number) throws Exception;

    void getAttendanceByNumberAndYearAndMonth() throws NotExistException;

}
