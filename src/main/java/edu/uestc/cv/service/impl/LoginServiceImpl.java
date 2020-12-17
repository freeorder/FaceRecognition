package edu.uestc.cv.service.impl;

import edu.uestc.cv.dao.UserDAO;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by HQC on 2020/10/14 22:45
 **/

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserDAO userDAO;

    @Override
    public boolean loginAuthentication(String username, String password) {
        try {
//            SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
//            // 登录后存放进shiro token
            UsernamePasswordToken token = new UsernamePasswordToken(
                    username, password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
//            session.setAttribute(SessionConstant.USERNAME, username);
            subject.getSession().setTimeout(1800000);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }

    }

    @Override
    public User getCurrentUserFromSession() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User) currentUser.getPrincipal();
        if (user != null) {
            user = userDAO.findUserByNumber(user.getNumber());
        }
        return user;

    }

//    @Override
//    public boolean logout() {
//        String username = (String) session.getAttribute(SessionConstant.USERNAME);
//        if (username != null) {
//            SecurityUtils.getSubject().logout();
//            session.removeAttribute(SessionConstant.USERNAME);
//            return true;
//        } else {
//            return false;
//        }
//    }

}