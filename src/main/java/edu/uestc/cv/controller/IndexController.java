package edu.uestc.cv.controller;

import edu.uestc.cv.constant.ResponseConstant;
import edu.uestc.cv.constant.SessionConstant;
import edu.uestc.cv.dao.UserDAO;
import edu.uestc.cv.entity.User;
import edu.uestc.cv.exception.NotExistException;
import edu.uestc.cv.service.LoginService;
import edu.uestc.cv.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * create by HQC on 2020/10/14 22:40
 **/

@RestController
public class IndexController {
    @Autowired
    private LoginService loginService;
    @Autowired
    HttpSession session;
    @Autowired
    UserDAO userDAO;
    /**
     * mongoDB里面存的是加密后的password. 加密算法是：password_DB = MD5(MD5(password)+salt)
     * 验证思路：客户端请求user.username，查询mongoDB, 如果存在，取出user.password_DB; 否则报错，用户不存在。
     * 取得password_DB后，将其与user.password加密后的信息比较，如果相等，登录成功；否则报错，密码错误。
     * @param number
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation("登录验证")
    public ResultUtil<String> login(@ApiParam(value = "用户名", required = true) @RequestParam String number,
                                    @ApiParam(value = "密码", required = true) @RequestParam String password) {
        try {
            if (loginService.loginAuthentication(number, password)) {
                User user=userDAO.findUserByNumber(number);
                //session.setAttribute(SessionConstant.USER_TYPE, SessionConstant.USER_TYPE_TEACHER);
                if (user!=null) {
                    session.setAttribute(SessionConstant.TEACHER_ID, user.getId());
                    session.setAttribute(SessionConstant.TEACHER_NUMBER, user.getNumber());
                    session.setAttribute(SessionConstant.TEACHER_NAME, user.getUsername());
                }
                return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "登录成功");
            } else {
                return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "用户名或密码错误");
            }
        } catch (NotExistException e) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.EXIST_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/login/getCurrentUser", method = RequestMethod.GET)
    @ApiOperation("获取当前登录用户状态")
    public ResultUtil getCurrentUser() {
        User user = loginService.getCurrentUserFromSession();
        if (user != null) {
            return new ResultUtil<>(ResponseConstant.ResponseCode.SUCCESS, "", user);
        } else {
            return new ResultUtil<>(ResponseConstant.ResponseCode.FAILURE, "", "登录状态异常!");
        }
    }

    @RequestMapping(value = "/getAuthStatement", method = RequestMethod.GET)
    @ApiOperation("前端使用空接口，接口内部返回空信息，ajax调用会被拦截器返回json，直接调用强制跳转首页")
    public ResultUtil nullMethod() {
        return new ResultUtil(ResponseConstant.ResponseCode.SUCCESS, "");
    }

//    @RequestMapping(value = "/logout", method = RequestMethod.GET)
//    @ApiOperation("退出登录")
//    public ResultUtil logout() {
//        boolean status = loginService.logout();
//        if (status) {
//            session.removeAttribute(SessionConstant.USERNAME);
//            return new ResultUtil(ResponseConstant.ResponseCode.SUCCESS, "", "退出登录成功!");
//        } else {
//            return new ResultUtil(ResponseConstant.ResponseCode.FAILURE, "", "登录状态异常!");
//        }
//    }
}
