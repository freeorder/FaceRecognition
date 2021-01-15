package edu.uestc.cv.dao;

import edu.uestc.cv.entity.Attendance;
import edu.uestc.cv.exception.ExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @Author: HQC
 * @Contact: qq545010683
 * @Data: 2020/12/19 21:02
 **/
@Repository
public class AttendanceDAO {

    @Autowired
    MongoTemplate mongoTemplate;

    public void addAttendance(Attendance attendance) throws Exception {
        Attendance attendanceInDB;

        attendanceInDB=findAttendanceByNumberAndYearAndMonth(attendance.getNumber(),attendance.getYear(),attendance.getMonth());
        if (attendanceInDB != null) {
            throw new ExistException(attendance.getNumber() + ":考勤月份重复\n");
        }
        mongoTemplate.insert(attendance);
    }

    public void deleteAttendance(Attendance attendance) {
        Query query = new Query(Criteria.where("_id").is(attendance.getId()));
        mongoTemplate.remove(query, Attendance.class);
    }

    public Attendance findAttendanceByNumberAndYearAndMonth(String number,String year,String month) {
        Query query = new Query();
        Criteria criteria = Criteria.where("number").is(number).and("year").is(year).and("month").is(month);
        query.addCriteria(criteria);

        return mongoTemplate.findOne(query, Attendance.class);
    }

    public void updateAttendance(Attendance attendance) {
        Update update = new Update();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(attendance.getId()));

        mongoTemplate.updateFirst(query, update, Attendance.class);
    }
}
