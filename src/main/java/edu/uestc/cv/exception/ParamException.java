package edu.uestc.cv.exception;

public class ParamException extends Exception {

    public ParamException(String msg) {
        super(msg);
    }

    public ParamException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
