package com.apin.third.service.info.push;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/18.
 */
public class PushMessage {

    private String title;
    private String content;

    private Map<String,Object> map=new HashMap<>();


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void putParam(String key,String value){
        map.put(key,value);
    }

//    @Override
//    public String toString(){
//        StringBuffer sb=new StringBuffer();
//        sb.append("{");
//        sb.append("title")
//    }


}
