package com.apin.interceptors;

import com.apin.common.jedis.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/8/27.
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private Redis redis;

    //实现预处理，可以进行编码、安全控制等处理
    //返回true的话请求继续向下流动，返回false的话，请求直接在这个地方就返回了
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }

    //实现后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //返回处理（已经渲染了页面），可以根据ex是否为null判断是否发生了异常，进行日志记录。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
