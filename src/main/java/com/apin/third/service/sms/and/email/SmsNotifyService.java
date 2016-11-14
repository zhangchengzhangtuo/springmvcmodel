package com.apin.third.service.sms.and.email;

import com.apin.third.service.http.HttpService;
import com.apin.utils.Constant;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SmsNotifyService extends NotifyProcessor{

    private static final Logger logger= LoggerFactory.getLogger(SmsNotifyService.class);

    private static final String SINGLE_URL="https://sms.yunpian.com/v2/sms/single_send.json";

    private static final String BATCH_URL="https://sms.yunpian.com/v2/sms/batch_send.json";

    private static final String apiKey="8dbf3d132c34d9e19299c61d89d85fad";

    private HttpService httpService;

    public HttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }


    /**
     *
     * @param title
     * @param body
     * @param users
     */
    @Override
    public void send(String title,String body,String... users){
        logger.debug("sms notify,title:{},body:{},users:{}", title, body, users);
        Map<String,String> headers=new HashMap<>();
        headers.put("Accept","application/json;charset=utf-8");
        headers.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8;");
        for(String user:users){
            Map<String,String> singleMsgBody=new HashMap<>();
            singleMsgBody.put("apikey",apiKey);
            singleMsgBody.put("mobile",user);
            singleMsgBody.put("text", body);
            try {
                HttpPost httpPost=httpService.formHttpPostWithNameValuePair(SINGLE_URL, headers, singleMsgBody);
                String response=httpService.response(httpPost);
                logger.debug("sms notify,title:{},body:{},users:{},response:{}",title,body,users,response);
            }catch(Throwable e){
                logger.error("fail to notify user:{},titile:{},body:{}",user,title,body);
            }
        }
    }


    /**
     *
     * @param modelName
     * @param title
     * @param params
     * @param phones
     */
    public void smsModelNotify(String modelName,String title,String params,String phones){

        String paramsWithoutColon="";
        String reg=":";
        Pattern pattern= Pattern.compile(reg);
        Matcher matcher=pattern.matcher(params);
        if(matcher.find()){
            paramsWithoutColon=params.replace(":","|");
        }else{
            paramsWithoutColon=params;
        }
        logger.info(paramsWithoutColon);
        String smsModel= Constant.DICT_SMS_MODEL_MAP.get(modelName);
        JSONArray paramsArray=new JSONArray(paramsWithoutColon);
        JSONArray phoneArray=new JSONArray(phones);

        List<String> paramsList=JSONToList(paramsArray);
        List<String> phoneList=JSONToList(phoneArray);

        String notifyMsg=replaceTokenInModel(smsModel, paramsList);

        notify(title, notifyMsg,phoneList.toArray(new String[phoneList.size()]));
    }

    private String replaceTokenInModel(String model,List<String> params){
        String reg="#\\w+#";
        Pattern pattern=Pattern.compile(reg);
        Matcher matcher=pattern.matcher(model);

        StringBuffer sb=new StringBuffer();
        if(params.size()>0) {
            for (int i = 0; matcher.find(); i++) {
                String param=params.get(i);
                String returnParam="";
                String returnReg="|";
                Pattern returnPattern= Pattern.compile(returnReg);
                Matcher returnMatcher=returnPattern.matcher(param);
                if(returnMatcher.find()){
                    returnParam=param.replace("|",":");
                }
                matcher.appendReplacement(sb,returnParam);
            }
            matcher.appendTail(sb);
        }
        logger.info("msg:{}", sb.toString());
        return sb.length()>0?sb.toString():model;
    }

    private List<String> JSONToList(JSONArray jsonArray){
        List<String> list=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            list.add(jsonArray.get(i).toString());
        }
        return list;
    }

}
