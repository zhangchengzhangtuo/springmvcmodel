package com.apin.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2016/8/16.
 */
public class ApinUtil {

    public static boolean isBlank(String ...params){

        if(params==null){
            return true;
        }

        for(String param:params){
            if(StringUtils.isBlank(param)){
                return true;
            }
        }

        return false;
    }
}
