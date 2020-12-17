package edu.uestc.cv.entity;

import java.util.Date;

/**
 * create by HQC on 2020/11/30 1:33
 **/
public class AttendanceDetail {
    private String id;//数据中的id是自动随机生成的，每个用户都有自己唯一的id
    private String number;//用户登陆编号
    private Date date;//考勤日期
    private String record;//备注

    public AttendanceDetail(String id, String number, Date date, String record) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.record = record;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "AttendanceDetail{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", date=" + date +
                ", record='" + record + '\'' +
                '}';
    }
}
