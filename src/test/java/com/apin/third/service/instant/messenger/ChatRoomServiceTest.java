package com.apin.third.service.instant.messenger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testChatRoom.xml")
public class ChatRoomServiceTest {

    @Resource
    private ChatRoomService chatRoomService;

    @Before
    public void setUp() throws Exception {

    }

//    @Test
    public void testCreate() throws Throwable {
        List<String> list=new ArrayList<>();
        list.add("APIN");
        String createId=chatRoomService.create("testtesttesttesttest","创建聊天室",10,"APIN",list.toArray(new String[list.size()]));
        System.out.println(createId);
    }

//    @Test
    public void testDelete() throws Throwable {
        String roomId="234202207342297516";
        boolean success=chatRoomService.delete(roomId);
        System.out.println(success);
    }
}