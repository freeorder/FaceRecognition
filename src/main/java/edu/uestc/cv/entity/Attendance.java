package edu.uestc.cv.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * create by HQC on 2020/11/30 1:32
 **/
public class Attendance {

    private String id;//数据中的id是自动随机生成的，每个用户都有自己唯一的id
    private String number;//工号
    private String year;//年份
    private String month;//月份
    private int attendance[];//当月出勤表 0：未考勤， 1：已考勤， 2：迟到，3：缺勤，4：请假，9：其他异常（第一个元素为0，第二个元素为1号）
    private int leave[];//当月离岗表 0 未考勤 1已考勤 2：早退，3：缺勤，4：请假，9：其他异常（第一个元素为0，第二个元素为1号）
    private List<AttendanceDetail> attendanceDetailList;//当月考勤详细情况表
    private int attendanceTime;//出勤天数
    private int lateTime;//迟到天数
    private int leaveEarlyTime;//早退天数
    private int offTime;//请假天数
    private int absentTime;//旷工天数

    public Attendance() {
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

    public int[] getAttendance() {
        return attendance;
    }

    public void setAttendance(int[] attendance) {
        this.attendance = attendance;
    }

    public int[] getLeave() {
        return leave;
    }

    public void setLeave(int[] leave) {
        this.leave = leave;
    }

    public List<AttendanceDetail> getAttendanceDetailList() {
        return attendanceDetailList;
    }

    public void setAttendanceDetailList(List<AttendanceDetail> attendanceDetailList) {
        this.attendanceDetailList = attendanceDetailList;
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
                ", attendance=" + Arrays.toString(attendance) +
                ", leave=" + Arrays.toString(leave) +
                ", attendanceDetailList=" + attendanceDetailList +
                ", attendanceTime=" + attendanceTime +
                ", lateTime=" + lateTime +
                ", leaveEarlyTime=" + leaveEarlyTime +
                ", offTime=" + offTime +
                ", absentTime=" + absentTime +
                '}';
    }
}
