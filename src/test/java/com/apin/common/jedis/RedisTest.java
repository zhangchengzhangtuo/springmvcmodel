package com.apin.common.jedis;

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

/**
 * Created by Administrator on 2016/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testRedis.xml")
public class RedisTest {

    @Resource
    private Redis redis;

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
    public void testGet() throws Throwable {
        client.set("clienttest","justTest");
        String str=redis.get(RedisType.client,"test");
        System.out.println(str);
        client.del("clienttest");
    }

    @Test
    public void testHget() throws Throwable {
        client.hset("clienttest", "name", "zhangcheng");
        String str=redis.hget(RedisType.client, "test", "name");
        System.out.println(str);
        client.del("clienttest");
    }

    @Test
    public void testHmget() throws Throwable {
        Map<String,String> map=new HashMap<>();
        map.put("name", "zhangcheng");
        map.put("age", "18");
        client.hmset("clienttest", map);
        List<String> fields=new ArrayList<>();
        fields.add("name");
        fields.add("age");
        List<String> list=redis.hmget(RedisType.client, "test", fields.toArray(new String[fields.size()]));
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
        client.close();
    }


}