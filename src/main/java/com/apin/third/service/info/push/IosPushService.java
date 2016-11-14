package com.apin.third.service.info.push;

import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.XingeApp;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/18.
 */
public class IosPushService implements IPushService{

    private Logger logger= LoggerFactory.getLogger(IosPushService.class);

    private static long iosAccessId=2200148331L;

    private static String iosSecretKey="85c97b96c74c7c9164c31a7636671389";

    private static int iosEnv=2;

    private XingeApp xingeApp;

    private  int env;

    public IosPushService(long accessId,String secretKey,int env){
        this.xingeApp=new XingeApp(accessId,secretKey);
        this.env=env;
    }

    public static IosPushService getInstance(){
        return InnerClass.instance;
    }

    static class InnerClass
    {
        private static IosPushService instance=new IosPushService(iosAccessId,iosSecretKey,iosEnv);
    }

    @Override
    public void push(PushMessage pushMessage, String... tokens) {
        MessageIOS messageIos=new MessageIOS();
        messageIos.setExpireTime(100);
        messageIos.setAlert(pushMessage.getContent());
        Map<String,Object> map=new HashMap<>();
        map.put("action", pushMessage.getTitle());
        map.putAll(pushMessage.getMap());
        messageIos.setCustom(map);

        for(String token:tokens){
            JSONObject response=xingeApp.pushSingleDevice(token,messageIos,env);
            logger.debug("response:{}",response);
        }


    }

    public void setXingeApp(XingeApp xingeApp) {
        this.xingeApp = xingeApp;
    }

    public void setEnv(int env) {
        this.env = env;
    }
}
