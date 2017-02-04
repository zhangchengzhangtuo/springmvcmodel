package com.apin.filters;

import com.apin.filters.dao.ApplicationMapper;
import com.apin.filters.po.ApinApplication;
import com.apin.utils.ApinUtil;
import com.apin.utils.ErrorEnum;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/1/11.
 */
public class AuthFilter implements Filter {

    private final static Logger logger= LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime=System.currentTimeMillis();
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        String applicationId=httpServletRequest.getHeader("applicationId");
        if(StringUtils.isBlank(applicationId)){
            writeResponse(response,startTime);
            return;
        }

        ApinApplication apinApplication=applicationMapper.selectApplicationById(applicationId);
        if(apinApplication==null){
            writeResponse(response,startTime);
            return;
        }

        if("POST".equalsIgnoreCase(httpServletRequest.getMethod())||"PUT".equalsIgnoreCase(httpServletRequest.getMethod())){
            String secret=apinApplication.getApplicationSecret();
            MyRequestWrapper myRequestWrapper=new MyRequestWrapper(httpServletRequest);
            String body=MyRequestWrapper.getBodyString(myRequestWrapper);
            JSONObject json=new JSONObject(body);
            System.out.println("body:"+json.toString());
            chain.doFilter(myRequestWrapper,response);
        }else{
            chain.doFilter(request,response);
        }

    }

    @Override
    public void destroy() {
        //do nothing
    }

    private void writeResponse(ServletResponse response,long startTime){
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter printWriter=null;
        try{
            printWriter=response.getWriter();
            JSONObject responseJson=new JSONObject();
            JSONObject responseHead=new JSONObject();
            JSONObject responseBody=new JSONObject();
            responseHead.put("code", ErrorEnum.ERROR_FAILED_VERIFIED.getErrorCode());
            responseHead.put("msg", ErrorEnum.ERROR_FAILED_VERIFIED.getErrorMsg());
            long currentTime=System.currentTimeMillis();
            long delayInMs=currentTime-startTime;
            float delayInS=(float)delayInMs/1000;
            String delayStr=String.valueOf(delayInS);
            responseHead.put("time",delayStr);
            responseJson.put("head",responseHead);
            responseJson.put("body",responseBody);
            printWriter.write(responseJson.toString());
            printWriter.flush();
        }catch (Throwable e){
            logger.error("error info:"+ ApinUtil.getTrace(e));
        }finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }
    }
}
