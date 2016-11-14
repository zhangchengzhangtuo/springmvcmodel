package com.apin.special.ticket.service;

import com.apin.common.jdbc.ApinMapper;
import com.apin.common.jdbc.SqlBuilder;
import com.apin.special.ticket.po.PinjiFlightInfo;
import com.apin.special.ticket.vo.FlightInfoVo;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class FlightQueryService {
    private static final Logger logger = LoggerFactory.getLogger(FlightQueryService.class);

    @Autowired
    private ApinMapper apinMapper;

    @Autowired
    private DozerBeanMapper beanMapper;


    public List<Map<String,Object>> getFlightInfoList(SqlBuilder sqlBuilder) throws Throwable{

        List<List<PinjiFlightInfo>> sectionList=new ArrayList<>();
        int index=0;
        List<PinjiFlightInfo> pinjiFlightInfoList=null;
        List<Integer> ids=new ArrayList<>();
        while(true){

            if(index==0){
                pinjiFlightInfoList=apinMapper.findAll(PinjiFlightInfo.class, sqlBuilder);
            }else{
                String searchSql="select * from apin_flight_info ";
                pinjiFlightInfoList=apinMapper.findAll(PinjiFlightInfo.class,SqlBuilder.select(searchSql).where("parent_id")
                        .in(ids.toArray(new Integer[ids.size()])));
            }

            if(pinjiFlightInfoList==null||pinjiFlightInfoList.size()==0){
                break;
            }

            sectionList.add(pinjiFlightInfoList);

            ids.clear();
            for(int i=0;i<pinjiFlightInfoList.size();i++){
                PinjiFlightInfo pinjiFlightInfo=pinjiFlightInfoList.get(i);
                ids.add(pinjiFlightInfo.getId());
            }
            index++;
        }


        List<Map<String,Object>> resultList=new ArrayList<>();
        int currentId;
        logger.debug("=============size:{}=========",sectionList.size());
        if(sectionList.size()==0){
            return resultList;
        }
        List<PinjiFlightInfo> headPinjiFlightInfoList=sectionList.get(0);
        for(int i=0;i<headPinjiFlightInfoList.size();i++){
            List<FlightInfoVo> flightInfo=new ArrayList<>();
            PinjiFlightInfo headPinjiFlightInfo=headPinjiFlightInfoList.get(i);
            FlightInfoVo headFlightInfoVo=new FlightInfoVo();
            beanMapper.map(headPinjiFlightInfo,headFlightInfoVo);
            flightInfo.add(headFlightInfoVo);
            currentId=headPinjiFlightInfo.getId();
            for(int j=1;j<sectionList.size();j++){
                List<PinjiFlightInfo> list=sectionList.get(j);
                int k=0;
                for(;k<list.size();k++){
                    PinjiFlightInfo pinjiFlightInfo=list.get(k);
                    if(currentId==pinjiFlightInfo.getParentId()){
                        FlightInfoVo flightInfoVo=new FlightInfoVo();
                        beanMapper.map(pinjiFlightInfo,flightInfoVo);
                        flightInfo.add(flightInfoVo);
                        currentId=pinjiFlightInfo.getId();
                        break;
                    }
                }
                if(k==list.size()){
                    break;
                }
            }
            Map<String,Object> map=new HashMap<>();
            map.put("flightInfo",flightInfo);
            resultList.add(map);
        }
        return resultList;
    }
}
