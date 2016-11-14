package com.apin.third.service.info.push;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/8/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testPushService.xml")
public class PushServiceTest {

    @Resource
    private PushService pushService;

    private Jedis client;

    private final static String host="120.26.85.57";

    private final static int port=6379;

    @Before
    public void setUp() throws Exception {
        client=new Jedis(host,port);
        client.auth("apin2015test");
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }



    @Test
    public void testPushMessage() throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("account","18817862033");
        map.put("osType","4");
        map.put("token", "bdf53f4222710d7d31ea69b63b24e1690629ac1dfbf5ffd662a326765dbcb067");
        client.hmset("client999999", map);

        Map<String,String> map1=new HashMap<>();
        map1.put("account","18626052087");
        map1.put("osType","4");
        map1.put("token", "95adf29ce34b4da52a18ce014b56a1b0a33cbfe68041da2c47e4392ae4b659bf");
        client.hmset("client888888",map1);

        PushMessage pushMessage=new PushMessage();
        pushMessage.setTitle("test");
        pushMessage.setContent("zhangcheng");

        List<String> userIds=new ArrayList<>();
        userIds.add("999999");
        userIds.add("888888");

        pushService.pushMessage(pushMessage,userIds.toArray(new String[userIds.size()]));

        client.del("client999999");
        client.del("client888888");
    }
}