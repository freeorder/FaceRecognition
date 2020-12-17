package edu.uestc.cv.constant;


public class SessionConstant {

    //登录过期时,重新登录后跳转到原来访问的页面
    public static final String REQUEST_URI = "REQUEST_URI";
    //用户类型,区分待定和员工
    public static final String USER_TYPE = "USER_TYPE";
    //用户类型:待定
    public static final String USER_TYPE_TEACHER = "TEACHER";
    //用户类型:员工
    public static final String USER_TYPE_STUDENT = "STAFF";
    //用户类型：管理员
    public static final String USER_TYPE_ADMIN="ADMIN";
    //员工ID
    public static final String STUDENT_ID = "STAFF_ID";
    //待定ID
    public static final String TEACHER_ID = "TEACHER_ID";
    //管理员ID
    public static final String ADMIN_ID = "ADMIN_ID";
    //登录的员工号
    public static final String STUDENT_NUMBER = "STAFF_NUMBER";
    //登录的待定工号
    public static final String TEACHER_NUMBER = "TEACHER_NUMBER";
    //登录的管理员账号
    public static final String ADMIN_NUMBER = "ADMIN_NUMBER";
    //登录待定姓名
    public static final String TEACHER_NAME = "TEACHER_NAME";
    //登录员工姓名
    public static final String STUDENT_NAME = "STAFF_NAME";
    //登录的管理员姓名
    public static final String ADMIN_NAME = "ADMIN_NAME";


}
