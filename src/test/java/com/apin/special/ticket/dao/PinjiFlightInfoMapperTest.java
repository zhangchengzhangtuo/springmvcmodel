package com.apin.special.ticket.dao;

import com.apin.special.ticket.po.*;
import com.apin.special.ticket.vo.FlightInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testPinjiFlightInfoMapper.xml")
public class PinjiFlightInfoMapperTest {

    @Resource
    private PinjiFlightInfoMapper pinjiFlightInfoMapper;

    @Resource
    private ClassMapper classMapper;

    @Test
    public void testGetFlightInfoByDepartDate() throws Exception {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=simpleDateFormat.format(date);

        //1.测试的是基本的PinjiFlightInfo,参数是基本格式String
//        List<PinjiFlightInfo> list=pinjiFlightInfoMapper.getFlightInfoByString(dateStr);
//        System.out.println("list.size:"+list.size());
//        for(int i=0;i<list.size();i++){
//            PinjiFlightInfo pinjiFlightInfo=list.get(i);
//            System.out.println("departDate:"+pinjiFlightInfo.getDepartDate());
//        }

//        2.测试的是FlightInfoVo，参数是基本格式String
//        List<FlightInfoVo> list=pinjiFlightInfoMapper.getFlightInfoByString(dateStr);
//        System.out.println("list.size:"+list.size());
//        for(int i=0;i<list.size();i++){
//            FlightInfoVo flightInfoVo=list.get(i);
//            System.out.println("flightInfo:" + flightInfoVo.toString());
//        }

        //3.测试多参数
//        List<FlightInfoVo> list1=pinjiFlightInfoMapper.getFlightInfoByMultiString(dateStr,"");
//        System.out.println("list1.size:"+list1.size());
//        for(int i=0;i<list1.size();i++){
//            FlightInfoVo flightInfoVo=list1.get(i);
//            System.out.println("flightInfo:" + flightInfoVo.toString());
//        }
//        List<FlightInfoVo> list2=pinjiFlightInfoMapper.getFlightInfoByMultiString("",dateStr);
//        System.out.println("list2.size:"+list2.size());
//        for(int i=0;i<list2.size();i++){
//            FlightInfoVo flightInfoVo=list2.get(i);
//            System.out.println("flightInfo:" + flightInfoVo.toString());
//        }
//        List<FlightInfoVo> list3=pinjiFlightInfoMapper.getFlightInfoByMultiString(dateStr,dateStr);
//        System.out.println("list3.size:"+list3.size());
//        for(int i=0;i<list3.size();i++){
//            FlightInfoVo flightInfoVo=list3.get(i);
//            System.out.println("flightInfo:" + flightInfoVo.toString());
//        }

        //4.参数的基本格式是实体类FlightInfoVo
//        FlightInfoVo flightInfoVo=new FlightInfoVo();
//        flightInfoVo.setDepartDate(dateStr);
//        flightInfoVo.setArriveDate(dateStr);
//        List<FlightInfoVo> list=pinjiFlightInfoMapper.getFlightInfoByEntity(flightInfoVo);
//        System.out.println("list.size:"+list.size());
//        for(int i=0;i<list.size();i++){
//            FlightInfoVo flightInfoVoTmp=list.get(i);
//            System.out.println("flightInfo:" + flightInfoVoTmp.toString());
//        }

        //5.参数的基本格式是MAP
//        Map<String,String> map=new HashMap<>();
//        map.put("ssh",dateStr);
//        map.put("ssj",dateStr);
//        List<FlightInfoVo> list=pinjiFlightInfoMapper.getFlightInfoByMap(map);
//        System.out.println("list.size:"+list.size());
//        for(int i=0;i<list.size();i++){
//            FlightInfoVo flightInfoVo=list.get(i);
//            System.out.println("flightInfo:"+flightInfoVo.toString());
//        }


        //6.1测试List
//        List<Integer> parentList=new ArrayList<>();
//        parentList.add(3);
//        parentList.add(5);
//        List<FlightInfoVo> childrenList=pinjiFlightInfoMapper.getFlightInfoByParentIdList(parentList);
//        System.out.println("childrenList.size:"+childrenList.size());
//        for(int i=0;i<childrenList.size();i++){
//            FlightInfoVo flightInfoVo=childrenList.get(i);
//            System.out.println("flightInfo:" + flightInfoVo.toString());
//        }
        //6.2测试Array
//        Integer [] parentArray=new Integer[] {3,5};
//        List<FlightInfoVo> childrenList1=pinjiFlightInfoMapper.getFlightInfoByParentIdArray(parentArray);
//        for(int i=0;i<childrenList1.size();i++){
//            FlightInfoVo flightInfoVo=childrenList1.get(i);
//            System.out.println("flightInfo:"+flightInfoVo.toString());
//        }

        //7.测试复用Sql语句
//        List<FlightInfoVo> list=pinjiFlightInfoMapper.getFlightInfoWithMutiplexSql(dateStr);
//        for(int i=0;i<list.size();i++){
//            FlightInfoVo flightInfoVo=list.get(i);
//            System.out.println("flightInfo:"+flightInfoVo.toString());
//        }

        //8.测试resultType为javaBean的情况，这个地方除了打印Id之外，其他的都是为空，因为与列名不一致。
//        PinjiFlightInfo pinjiFlightInfo=pinjiFlightInfoMapper.getFlightInfoReturnWithEntity(3);
//        if(null==pinjiFlightInfo){
//            System.out.println("pinjiFlightInfo is null");
//        }else{
//            System.out.println("id:"+pinjiFlightInfo.getId());
//        }

        //9.测试插入一条数据
//        FlightInfo flightInfo=new FlightInfo();
//        flightInfo.setDepartDate("2016-10-01");
//        flightInfo.setDepartPlace("杭州");
//        flightInfo.setDepartPlaceCode("HGH");
//        flightInfo.setDestPlace("上海");
//        flightInfo.setDestPlaceCode("SHA");
//        int num=pinjiFlightInfoMapper.insertFlightInfo(flightInfo);
//        System.out.println("insert number:"+num);

        //10.测试插入部分数据
//        FlightInfo flightInfo=new FlightInfo();
//        flightInfo.setDepartDate("2016-10-01");
//        int num=pinjiFlightInfoMapper.insertSelectFlightInfo(flightInfo);
//        System.out.println("insert number:"+num);

        //11.测试更新数据
//        FlightInfo flightInfo=new FlightInfo();
//        flightInfo.setDepartDate("2016-10-02");
//        flightInfo.setDepartPlace("上海");
//        flightInfo.setDepartPlaceCode("SHA");
//        flightInfo.setDestPlace("杭州");
//        flightInfo.setDestPlaceCode("HGH");
//        flightInfo.setId(57);
//        int num=pinjiFlightInfoMapper.updateFlightInfo(flightInfo);
//        System.out.println("update number:"+num);

        //12.测试更新部分数据
//        FlightInfo flightInfo=new FlightInfo();
//        flightInfo.setDepartDate("2016-10-03");
//        flightInfo.setId(57);
//        int num=pinjiFlightInfoMapper.updateSelectFlightInfo(flightInfo);
//        System.out.println("update number:"+num);

        //13.测试删除数据
//        int num=pinjiFlightInfoMapper.deleteFlightInfo(57);
//        System.out.println("delete number:"+num);

        //14.测试通过构造方法生成resultMap
//        FlightInfoConstruct flightInfoConstruct=pinjiFlightInfoMapper.getFlightInfoWithConstructor(55);
//        System.out.println("flightInfoWithConstruct:"+flightInfoConstruct.toString());

        //15.1测试通过association联合的select方法实现
//        ClassRoom classRoom=classMapper.getClassRoomWithSelectMethod(1);
//        Teacher teacher=classRoom.getTeacher();
//        System.out.println("teacher:"+teacher.toString());

        //15.2测试通过association联合的resultMap方法实现
//        ClassRoom classRoom1=classMapper.getClassRoomWithResultMapMethod(1);
//        Teacher teacher1=classRoom1.getTeacher();
//        System.out.println("teacher1:"+teacher1.toString());

        //16.1测试通过collection聚集的select方法实现
//        ClassRoomWithStudent classRoomWithStudent=classMapper.getClassRoomWithStudentWithSelectMethod(1);
//        List<Student> list=classRoomWithStudent.getStudents();
//        System.out.println(list.size());
//        System.out.println(classRoomWithStudent);

        //16.2测试通过collection聚集的resultMap方法实现，这个地方有点问题：查出来的结果与select方法查出来的不一致
        //错误表象是：本来有三个student符合条件，但是查出来的只有一个，
        ClassRoomWithStudent classRoomWithStudent1=classMapper.getClassRoomWithStudentWithResultMapMethod(1);
        List<Student> list=classRoomWithStudent1.getStudents();
        System.out.println(list.size());
        System.out.println(classRoomWithStudent1);

    }




























}