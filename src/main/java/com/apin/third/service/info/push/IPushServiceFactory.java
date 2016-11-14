package com.apin.third.service.info.push;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface IPushServiceFactory {

    public IPushService createPushService(String osType) throws Throwable;
}
