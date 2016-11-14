package com.apin.third.service.timer;

import com.apin.common.jdbc.ApinMapper;
import com.apin.common.jdbc.SqlBuilder;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sitong.thinker.common.util.StDateUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Timestamp;
import java.util.*;


/**
 * @author zhangcheng
 */
@Service
public class TimerTaskManagement {

    private static final Logger logger= LoggerFactory.getLogger(TimerTaskManagement.class);

    @Value("${quartz.threadPool.thread.count}")
    private String threadCount;

    @Value("${org.quartz.dataSource.apin.driver}")
    private String driver;

    @Value("${org.quartz.dataSource.apin.URL}")
    private String url;

    @Value("${org.quartz.dataSource.apin.user}")
    private String user;

    @Value("${org.quartz.dataSource.apin.password}")
    private String password;

    @Value("${org.quartz.dataSource.apin.maxConnections}")
    private String maxConnections;

    @Autowired
    private ApinMapper apinMapper;

    private MyJobFactory myJobFactory;

    @Autowired
    public void setMyJobFactory(MyJobFactory myJobFactory){
        this.myJobFactory=myJobFactory;
    }

    SchedulerFactory sf;

    @PostConstruct
    public void init() throws Throwable{
        logger.debug("instantiate the StdSchedulerFactory");
        Properties properties=new Properties();
        properties.put("org.quartz.scheduler.instanceName","MyScheduler");
        properties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", threadCount);
        properties.put("org.quartz.scheduler.jobFactory.class","com.apin.pinji.Timer.MyJobFactory");
        properties.put("org.quartz.scheduler.jobFactory",myJobFactory);
        properties.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.put("org.quartz.jobStore.driverDelegateClass","org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.put("org.quartz.jobStore.tablePrefix","QRTZ_");
        properties.put("org.quartz.jobStore.dataSource","apin");
        properties.put("org.quartz.dataSource.apin.driver", driver);
        properties.put("org.quartz.dataSource.apin.URL",url);
        properties.put("org.quartz.dataSource.apin.user",user);
        properties.put("org.quartz.dataSource.apin.password",password);
        properties.put("org.quartz.dataSource.apin.maxConnections",10);
        sf=new StdSchedulerFactory(properties);
    }

    public void addTimerTask(Class<?> taskClass,TimerCatagory timerCatagory,String name,long durationInMs,Map<String,String> map)throws SchedulerException{
        Date currentDate=new Date();
        long endDateInMs=currentDate.getTime()+durationInMs;
        Date endDate=new Date(endDateInMs);
        logger.info("BiddingTask endDate:{}",endDate.toString());
        addTimerTask(taskClass, timerCatagory, name, endDate, map);
    }

    public void addTimerTask(Class<?> taskClass,TimerCatagory timerCatagory,String name,Date endDate,Map<String,String> map) throws SchedulerException{
        //1.创建工作对象
        JobDetail jobDetail=new JobDetail();
        jobDetail.setJobClass(taskClass);
        jobDetail.setName("job" + name);
        jobDetail.setGroup("jobGroup" + timerCatagory.getName());
        jobDetail.getJobDataMap().putAll(map);
        jobDetail.addJobListener("jobListener");


        //2.创建Trigger对象
        SimpleTrigger simpleTrigger=new SimpleTrigger();
        simpleTrigger.setName("trigger" + name);
        simpleTrigger.setGroup("triggerGroup" + timerCatagory.getName());
        simpleTrigger.setStartTime(endDate);

        //3.创建Scheduler对象，并配置JobDetail和Trigger对象
        Scheduler scheduler=sf.getScheduler();
        scheduler.addJobListener(new TaskListener());
        scheduler.scheduleJob(jobDetail, simpleTrigger);
        scheduler.start();
    }

    public void removeTimerTask(TimerCatagory timerCatagory,String name) throws SchedulerException{
        Scheduler scheduler=sf.getScheduler();
        //1.停止触发器
        scheduler.pauseTrigger("trigger"+name,"triggerGroup"+timerCatagory.getName());
        //2.移除触发器
        scheduler.unscheduleJob("trigger"+name,"triggerGroup"+timerCatagory.getName());
        //3.删除任务
        scheduler.deleteJob("job"+name,"jobGroup"+timerCatagory.getName());
    }

    @PreDestroy
    public void close(){
        try {
            Scheduler scheduler = sf.getScheduler();
            scheduler.shutdown(true);
        }catch(SchedulerException e){
            logger.error("Exception caught when close the scheduler");
        }

    }

    public enum TimerCatagory{
        BIDDINGTASK("biddingTask"),
        ORDERTASK("orderTask");

        private String name;

        TimerCatagory(String name){
            this.name=name;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class TaskListener implements JobListener{

        @Override
        public String getName() {
            return "jobListener";
        }

        @Override
        public void jobToBeExecuted(JobExecutionContext context) {
            String jobName=context.getJobDetail().getJobDataMap().getString("jobName");
            String jobGroupName=context.getJobDetail().getJobDataMap().getString("jobGroupName");
            logger.info("Group:{},job:{} is to be excuted", jobGroupName, jobName);
        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext context) {

        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
            logger.info("TaskListener is executing");
            String jobName=context.getJobDetail().getJobDataMap().getString("jobName");
            String jobGroupName=context.getJobDetail().getJobDataMap().getString("jobGroupName");
            if(null==jobException) {
                logger.info("Group:{},job:{} has been successful to excute", jobGroupName, jobName);
            }else{
                logger.error("Exception:{} has been caught when Group:{},job:{} was executed",jobException,jobGroupName,jobName);
            }
            //job执行完毕之后如果jobDetail还存在而trigger已经不存在了，因此可以将jobDetail删除
            logger.info("Group:{},job:{} is to be removed", jobGroupName, jobName);
            try {
                sf.getScheduler().deleteJob(jobName, jobGroupName);
            }catch(SchedulerException e){
                logger.error("Exception has been caught when delete Group:{},job:{}",jobGroupName,jobName);
            }
        }
    }
}
