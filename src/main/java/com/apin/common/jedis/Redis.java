package com.apin.common.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
@Service
public class Redis {

    private static final Logger logger= LoggerFactory.getLogger(Redis.class);

    @Autowired
    private RedisExecuteTemplate redisExecuteTemplate;

    public void setRedisExecuteTemplate(RedisExecuteTemplate redisExecuteTemplate) {
        this.redisExecuteTemplate = redisExecuteTemplate;
    }

    /**
     *
     * @param redisType
     * @param key
     * @return
     * @throws Throwable
     */
    public String get(RedisType redisType,String key) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (String)redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            public Object command(Jedis jedis) {
                return jedis.get(redisKey);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @param field
     * @return
     * @throws Throwable
     */
    public String hget(RedisType redisType,String key, final String field) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (String)redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.hget(redisKey, field);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @param fields
     * @return
     * @throws Throwable
     */
    public List<String> hmget(RedisType redisType,String key, final String... fields) throws  Throwable{
        final String redisKey=redisType.toString()+key;
        return (List<String>) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.hmget(redisKey,fields);
            }
        });
    }





}
