package edu.uestc.cv.entity;

import java.util.Date;

/**
 * create by HQC on 2020/11/30 1:32
 **/
public class Attendance {

    private String id;//数据中的id是自动随机生成的，每个用户都有自己唯一的id
    private String number;//用户登陆编号
    private String year;//年份
    private String month;//月份
    private int attendanceTime;//出勤天数
    private int lateTime;//迟到天数
    private int leaveEarlyTime;//早退天数
    private int offTime;//请假天数
    private int absentTime;//旷工天数

    public Attendance(String id, String number, String year, String month, int attendanceTime, int lateTime, int leaveEarlyTime, int offTime, int absentTime) {
        this.id = id;
        this.number = number;
        this.year = year;
        this.month = month;
        this.attendanceTime = attendanceTime;
        this.lateTime = lateTime;
        this.leaveEarlyTime = leaveEarlyTime;
        this.offTime = offTime;
        this.absentTime = absentTime;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(int attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public int getLateTime() {
        return lateTime;
    }

    public void setLateTime(int lateTime) {
        this.lateTime = lateTime;
    }

    public int getLeaveEarlyTime() {
        return leaveEarlyTime;
    }

    public void setLeaveEarlyTime(int leaveEarlyTime) {
        this.leaveEarlyTime = leaveEarlyTime;
    }

    public int getOffTime() {
        return offTime;
    }

    public void setOffTime(int offTime) {
        this.offTime = offTime;
    }

    public int getAbsentTime() {
        return absentTime;
    }

    public void setAbsentTime(int absentTime) {
        this.absentTime = absentTime;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", attendanceTime=" + attendanceTime +
                ", lateTime=" + lateTime +
                ", leaveEarlyTime=" + leaveEarlyTime +
                ", offTime=" + offTime +
                ", absentTime=" + absentTime +
                '}';
    }
}
