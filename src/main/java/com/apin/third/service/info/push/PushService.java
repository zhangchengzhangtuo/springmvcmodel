package com.apin.third.service.info.push;

import com.apin.common.jedis.Redis;
import com.apin.common.jedis.RedisType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/8/18.
 */
public class PushService{

    private Logger logger= LoggerFactory.getLogger(PushService.class);

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private Redis redis;

    private String [] fields={"osType","token"};


    public void push(PushMessage pushMessage, String... userIds) {
        PushWorker pushWorker=new PushWorker(pushMessage,userIds);
        try{
            threadPoolTaskExecutor.execute(pushWorker);
        }catch(Throwable e){
            logger.debug("fail to push message",e.getCause());
        }

    }

    public void pushMessage(PushMessage pushMessage,String... userIds){
        try {
            Map<String,List<String>> map=new HashMap<>();
            for (String userId : userIds) {
                List<String> clientInfo = redis.hmget(RedisType.client, userId, fields);
                String osType=clientInfo.get(0);
                String token=clientInfo.get(1);
//                String clientInfo=redis.get(RedisType.client,userId);
//                JSONObject jsonObject=new JSONObject(clientInfo);
//                String osType=jsonObject.getString("osType");
//                String token=jsonObject.getString("token");

                if(map.containsKey(osType)){
                    List<String> list=map.get(osType);
                    list.add(token);
                }else{
                    List<String> list=new ArrayList<>();
                    list.add(token);
                    map.put(osType, list);
                }
            }

            for(String osType:map.keySet()){
                List<String> tokensList=map.get(osType);
                IPushService pushServiceInstance=PushServiceFactory.getInstance().createPushService(osType);
                if(logger.isDebugEnabled()){
                    logger.debug("push message to osType:{},pushMessage:{},tokens:{}",osType,pushMessage,tokensList.toString());
                }
                pushServiceInstance.push(pushMessage,tokensList.toArray(new String[tokensList.size()]));
            }

        }catch(Throwable e){
            logger.error("fail to push message:{}",e.getStackTrace());
        }

    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void setRedis(Redis redis) {
        this.redis = redis;
    }

    private class PushWorker implements Runnable{

        private PushMessage pushMessage;

        private String [] userIds;

        public PushWorker(PushMessage pushMessage,String... users){
            this.pushMessage=pushMessage;
            this.userIds=users;
        }

        public PushMessage getPushMessage() {
            return pushMessage;
        }

        public void setPushMessage(PushMessage pushMessage) {
            this.pushMessage = pushMessage;
        }

        public String[] getUsers() {
            return userIds;
        }

        public void setUsers(String[] users) {
            this.userIds = users;
        }


        @Override
        public void run() {
            pushMessage(pushMessage,userIds);
        }
    }
}
