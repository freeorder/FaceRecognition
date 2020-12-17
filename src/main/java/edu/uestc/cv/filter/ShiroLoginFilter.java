package edu.uestc.cv.filter;


import com.alibaba.fastjson.JSON;
import edu.uestc.cv.constant.ResponseConstant;
import edu.uestc.cv.util.ResultUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShiroLoginFilter extends FormAuthenticationFilter {

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String contextPath = httpServletRequest.getContextPath();
        if (isAjax(request)) {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            ResultUtil res = new ResultUtil(ResponseConstant.ResponseCode.UNAUTHORIZED_RESOURCES,"您未通过认证或认证状态超时,请重新登录!");
            httpServletResponse.getWriter().write(JSON.toJSONString(res));
        } else {
            httpServletResponse.sendRedirect(contextPath);
        }
        return false;
    }

    private boolean isAjax(ServletRequest request){
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(header)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}