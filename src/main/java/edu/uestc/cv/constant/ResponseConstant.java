package edu.uestc.cv.constant;

public class ResponseConstant {
    public static class ResponseCode {
        //成功
        public static final int SUCCESS = 0;
        //请求参数异常
        public static final int PARAM_ERROR = 1;
        // 禁止访问(权限不够)
        public static final int FORBIDDEN_ERROR = 2;
        // 已经存在或者不存在
        public static final int EXIST_ERROR = 3;
        // 服务器错误
        public static final int SERVER_ERROR = 4;
        // 数据库错误
        public static final int DB_ERROR = 5;
        // 未知错误
        public static final int UNKOWN_ERROR = 6;
        // 状态异常
        public static final int STATE_ERROR = 7;
        // 超出限制
        public static final int LIMITION_ERROR = 8;
        // 未登录
        public static final int NOT_LOGIN = 9;
        //失败
        public static final int FAILURE = 10;

        public static final int NEED_MODIFY_PASSWORD = 11;

        public static final int UNAUTHORIZED_RESOURCES = 12;
    }
}
