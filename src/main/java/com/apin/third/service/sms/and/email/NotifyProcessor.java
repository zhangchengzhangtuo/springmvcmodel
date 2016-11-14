package com.apin.third.service.sms.and.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by Administrator on 2016/8/17.
 */
public class NotifyProcessor implements INotifyService{

    //适配器模式

    private static final Logger logger= LoggerFactory.getLogger(NotifyProcessor.class);

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }


    @Override
    public void notify(String title, String body, String... users) {
        NotifyWorker worker=new NotifyWorker(title,body,users);
        try {
            threadPoolTaskExecutor.execute(worker);
        }catch (TaskRejectedException e){
            logger.error("fail to notify the client",e.getCause());
        }
    }

    public void send(String title,String body,String... users){
    }



    private class NotifyWorker implements Runnable{
        private String title;
        private String body;
        private String [] users;

        public NotifyWorker(String title,String body,String... users){
            this.title=title;
            this.body=body;
            this.users=users;
        }

        @Override
        public void run() {
            send(title,body,users);
        }
    }
}
