package com.apin.special.ticket.dao;

import com.apin.special.ticket.po.FlightInfo;
import com.apin.special.ticket.po.FlightInfoConstruct;
import com.apin.special.ticket.po.PinjiFlightInfo;
import com.apin.special.ticket.vo.FlightInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/27.
 */
public interface PinjiFlightInfoMapper {
    //对应的是BaseResultMap,参数类型是基本类型String
//    public List<PinjiFlightInfo> getFlightInfoByString(String departDate);

    //对应的是specialTicketResultMap，参数类型是基本类型String
    public List<FlightInfoVo> getFlightInfoByString(String departDate);

    //根据出发日期或者抵达日期查询，出发日期、抵达日期有可能为空
    // (多参数的时候则需要在接口的参数上添加@Param注解)
    public List<FlightInfoVo> getFlightInfoByMultiString(@Param(value="departDate")String departDate,@Param(value="arriveDate")String arriveDate);

    //参数类型是Entity
    public List<FlightInfoVo> getFlightInfoByEntity(FlightInfoVo flightInfoVo);

    //参数类型是Map
    public List<FlightInfoVo> getFlightInfoByMap(Map<String,String> map);

    //根据List查找，这个一般主要用于in的SQL语句
    public List<FlightInfoVo> getFlightInfoByParentIdList(List<Integer> parentIdList);
    //根据Array查找，一般也用于in的SQL语句
    public List<FlightInfoVo> getFlightInfoByParentIdArray(Integer[] parentIdArray);

    //复用Sql语句
    public List<FlightInfoVo> getFlightInfoWithMutiplexSql(String departDate);

    //resultType是javaBean时候，这个地方有个问题需要注意。
    public PinjiFlightInfo getFlightInfoReturnWithEntity(Integer departDate);

    //插入一个新的航班信息
    public int insertFlightInfo(FlightInfo flightInfo);

    //插入部分航班信息
    public int insertSelectFlightInfo(FlightInfo flightInfo);

    //更新航班信息
    public int updateFlightInfo(FlightInfo flightInfo);

    //更新部分航班信息
    public int updateSelectFlightInfo(FlightInfo flightInfo);

    //删除航班信息
    public int deleteFlightInfo(int id);

    //通过构造Map获取航班信息
    public FlightInfoConstruct getFlightInfoWithConstructor(int id);




}
