package com.apin.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/16.
 */
public class ApinUtil {

    private static final Logger logger= LoggerFactory.getLogger(ApinUtil.class);

    public static boolean isBlank(JSONObject paramsJson,String...params){

        for(String param:params){
            if(!paramsJson.has(param)){
                logger.error("The key of param:{} doesnot exist",param);
                return true;
            }
            Object object=paramsJson.get(param);
            if(null==object){
                logger.error("The value of param:{} doesnot exist",param);
                return true;
            }
            if(object instanceof String){
                if(StringUtils.isBlank((String) object)){
                    logger.error("The value of param:{} is ''",param);
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isMobileTel(String userPhone){
        Pattern pattern=Pattern.compile("^((13[0-9])|(15[^4\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher matcher=pattern.matcher(userPhone);
        return matcher.matches();
    }

    public static HashMap<String,Object> toHashMap(JSONObject json){
        HashMap<String,Object> map=new HashMap<>();
        Iterator it=json.keys();
        while(it.hasNext()){
            String key=String.valueOf(it.next());
            Object object=json.get(key);
            map.put(key,object);
        }
        return map;
    }

    public static Object toObject(Class<?> clazz,JSONObject json)throws Throwable{
        Object object=clazz.newInstance();
        Class objectClass=object.getClass();
        Field[] fields=objectClass.getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            Field field=fields[i];
            field.setAccessible(true);
            if(json.has(field.getName())) {
                String type = field.getType().toString();
                if (type.endsWith("String")) {
                    field.set(object, json.getString(field.getName()));
                } else if (type.endsWith("int") || type.endsWith("Integer")) {
                    field.set(object, json.getInt(field.getName()));
                } else if (type.endsWith("long") || type.endsWith("Long")) {
                    field.set(object, json.getLong(field.getName()));
                } else {
                    field.set(object, json.get(field.getName()));
                }
            }
        }
        return object;
    }


    /**
     * ÈÕÖ¾´òÓ¡
     * @param t
     * @return
     */
    public static String getTrace(Throwable t){
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        StringBuffer sb=stringWriter.getBuffer();
        return sb.toString();
    }


}
