package com.apin.third.service.info.push;

import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/8/18.
 */
public class AndriodPushService implements IPushService{

    private final static Logger logger= LoggerFactory.getLogger(AndriodPushService.class);

    private static final int ANDRIOD_EXPIRE_TIME=60;

    private static final Style vibrateAndRing = new Style(0, 1, 1, 1, 0);

    private static final Style silence=new Style(0,0,0,1,0);

    private static long andriodAccessId= 2100139073L;

    private static String andriodSecretKey="2a08d5c67ba00c5a6e051f7ddde2ff6d";

    private XingeApp xingeApp;

    public AndriodPushService(long accessId,String secretKey){
        xingeApp=new XingeApp(accessId,secretKey);
    }

    public static AndriodPushService getInstance(){
        return InnerClass.instance;
    }

    static class InnerClass
    {
        private static AndriodPushService instance=new AndriodPushService(andriodAccessId,andriodSecretKey);
    }

    @Override
    public void push(PushMessage pushMessage, String... tokens) {
        Message message=new Message();
        message.setType(Message.TYPE_NOTIFICATION);
        //
        message.setTitle(pushMessage.getTitle());
        //
        message.setContent(pushMessage.getContent());
        message.setExpireTime(ANDRIOD_EXPIRE_TIME);
        message.setStyle(vibrateAndRing);
        message.setCustom(pushMessage.getMap());

        for(String token :tokens){
            JSONObject response=xingeApp.pushSingleDevice(token,message);
            logger.debug("token:{},response:{}",token,response);
        }


//        xingeApp.pushSingleDevice(message,);

//        ClickAction action=new ClickAction();
//        action.setActionType();
//        action.setUrl();
//        action.setConfirmOnUrl();
//        message.setAction(action);

//        TimeInterval acceptTime1=new TimeInterval(12,0,13,59);
//        TimeInterval acceptTime2=new TimeInterval(19,0,20,59);
//        message.addAcceptTime(acceptTime1);
//        message.addAcceptTime(acceptTime2);

    }

    public void setXingeApp(XingeApp xingeApp) {
        this.xingeApp = xingeApp;
    }
}
