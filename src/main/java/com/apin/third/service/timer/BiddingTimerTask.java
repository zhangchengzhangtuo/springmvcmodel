package com.apin.third.service.timer;


import com.apin.common.jdbc.ApinMapper;
import com.apin.common.jdbc.SqlBuilder;
import com.apin.third.service.instant.messenger.ChatRoomService;
import com.apin.utils.ApinResultModel;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sitong.thinker.common.util.StDateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BiddingTimerTask extends TimerTask{
    private final static Logger logger= LoggerFactory.getLogger(BiddingTimerTask.class);

    @Autowired
    private ApinMapper apinMapper;

    @Autowired
    private ChatRoomService chatRoomService;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map=context.getJobDetail().getJobDataMap();
        String groupId=map.getString("roomId");
        String userId=map.getString("userId");
        //1.修改状态为'0'，竞拍已经结束，商户不能再出价
        String updateClause = "update APIN_BIDDING set HANDLE_STATUS=?,UPDATE_TIME=? where GROUP_ID=?";
        List<Object> params=new ArrayList<>();
        params.add("3");
        params.add(StDateUtils.format(new Date(), StDateUtils.FORMAT_yyyy_MM_dd_HH_mm_ss));
        params.add(groupId);

        try {
            apinMapper.save(SqlBuilder.update(updateClause, params.toArray(new Object[params.size()])));
        }catch (Throwable e){
            logger.error("Exception:{} caught when update the handle_status to 0 with room:{},userId:{}",e.getCause(),groupId,userId);
        }

        //2.删除房间
        try {
            if(!chatRoomService.delete(groupId)){
                throw new Throwable("fail to delete chatRoom");
            }
        } catch (Throwable e) {
            logger.error("Exception:{} caught when delete the room with groupId:{},userId:{}",e.getCause(), groupId, userId);
        }

    }
}
