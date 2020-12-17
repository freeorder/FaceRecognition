package edu.uestc.cv.exception;

public class ExistException extends Exception {

    public ExistException(String msg) {
        super(msg);
    }

    public ExistException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
