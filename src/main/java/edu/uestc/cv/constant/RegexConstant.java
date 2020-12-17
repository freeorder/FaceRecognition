package edu.uestc.cv.constant;

import java.util.regex.Pattern;

public class RegexConstant {
    //用户名（汉字、字母、数字的组合）2-20位
    private static final String USERNAME = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,20}$";
    //密码（6-16位数字、字母和特殊字符的组合，特殊字符只能在!@#$%^&*?,.中）
    private static final String PASSWORD = "^[0-9A-Za-z!@#$%^&*\\?,\\.]{6,16}$";
    //用户编号,6-16位的正数
    private static final String USER_NUMBER = "^[1-9]+[0-9]{5,15}$";
    //各种标题, 2-20个非空白字符
    private static final String TITLE = "^[\\S]{2,20}$";

    public static final Pattern USERNAME_PATTERN = Pattern.compile(USERNAME);
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD);
    public static final Pattern USER_NUMBER_PATTERN = Pattern.compile(USER_NUMBER);
    public static final Pattern TITLE_PATTERN = Pattern.compile(TITLE);
}
