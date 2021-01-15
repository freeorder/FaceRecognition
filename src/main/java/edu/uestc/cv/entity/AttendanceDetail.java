package edu.uestc.cv.entity;

import java.util.Date;

/**
 * create by HQC on 2020/11/30 1:33
 **/
public class AttendanceDetail {
    private String id;//数据中的id是自动随机生成的，每个用户都有自己唯一的id
    private int day;//出勤当天号数
    private int attentdanceStatus;//出勤状态
    private int leaveStatus;//离岗状态
    private Date attendanceDate;//出勤时间
    private Date leaveDate;//离岗时间
    private String record;//备注

    public AttendanceDetail() {
    }

    public AttendanceDetail(String id, int day, int attentdanceStatus, int leaveStatus, Date attendanceDate, Date leaveDate, String record) {
        this.id = id;
        this.day = day;
        this.attentdanceStatus = attentdanceStatus;
        this.leaveStatus = leaveStatus;
        this.attendanceDate = attendanceDate;
        this.leaveDate = leaveDate;
        this.record = record;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getAttentdanceStatus() {
        return attentdanceStatus;
    }

    public void setAttentdanceStatus(int attentdanceStatus) {
        this.attentdanceStatus = attentdanceStatus;
    }

    public int getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(int leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
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
                ", day=" + day +
                ", attentdanceStatus=" + attentdanceStatus +
                ", leaveStatus=" + leaveStatus +
                ", attendanceDate=" + attendanceDate +
                ", leaveDate=" + leaveDate +
                ", record='" + record + '\'' +
                '}';
    }
}
