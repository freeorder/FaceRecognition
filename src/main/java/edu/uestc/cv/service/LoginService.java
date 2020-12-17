package edu.uestc.cv.service;

import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;

/**
 * create by HQC on 2020/10/14 22:44
 **/
public interface LoginService {

    boolean loginAuthentication(String username, String password) throws NotExistException;

    User getCurrentUserFromSession();
}
