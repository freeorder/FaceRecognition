package edu.uestc.cv.exception;

import com.alibaba.fastjson.JSON;
import edu.uestc.cv.constant.ResponseConstant;
import edu.uestc.cv.util.ResultUtil;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> processUnauthenticatedException(NativeWebRequest request, UnauthorizedException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=UTF-8");
        return ResponseEntity.status(200).headers(headers).body(JSON.toJSONString(new ResultUtil(ResponseConstant.ResponseCode.UNAUTHORIZED_RESOURCES, "您访问了未授权的请求！请检查当前登录账号权限。")));
    }
}