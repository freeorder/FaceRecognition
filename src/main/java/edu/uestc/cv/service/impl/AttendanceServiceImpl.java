package edu.uestc.cv.service.impl;

import edu.uestc.cv.exception.ExistException;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.service.AttendanceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: HQC
 * @Contact: qq545010683
 * @Data: 2020/12/16 23:11
 **/
@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Override
    public void addAttendance() throws ExistException, NotExistException {

    }

    @Override
    public void updateAttendance() throws NotExistException {

    }

    @Override
    public List<String> listAllAttendance() {
        return null;
    }

    @Override
    public void deleteAttendance(List<String> number) throws Exception {

    }

    @Override
    public void getAttendanceByNumberAndYearAndMonth() throws NotExistException {

    }
}
