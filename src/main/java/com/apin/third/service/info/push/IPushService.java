package com.apin.third.service.info.push;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface IPushService {


    /**
     *
     * @param pushMessage
     * @param tokens
     */
    public void push(PushMessage pushMessage,String... tokens);
}
