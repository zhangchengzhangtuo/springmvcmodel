package com.apin.special.ticket.service;

import com.apin.common.jdbc.SqlBuilder;
import com.apin.utils.ApinDateUtil;
import com.apin.utils.ApinResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/14.
 */
@Service
public class SpecialTicketService {

    private static final Logger logger= LoggerFactory.getLogger(SpecialTicketService.class);

    @Autowired
    private FlightQueryService flightQueryService;

    public void getSpecialTicketFlights(ApinResultModel model,String userId,int pageNo,int pageSize) throws Throwable {
        String currentDate = ApinDateUtil.format(new Date(),1);
        StringBuffer buffer = new StringBuffer("select * from APIN_FLIGHT_INFO ");
        buffer.append(" WHERE parent_id='0' and REMAIN_TICKET_NUM>0 AND DEPART_DATE>='" + currentDate + "'");
        String sqlStr=buffer.toString();
        List<Map<String,Object>> pinjiFlightInfoList=flightQueryService.getFlightInfoList(SqlBuilder.select(sqlStr).limit(pageNo * pageSize, pageSize));
        Map<String,Object> map=new HashMap<>();
        map.put("flightInfoList",pinjiFlightInfoList);
        model.setData(map);
    }
}
