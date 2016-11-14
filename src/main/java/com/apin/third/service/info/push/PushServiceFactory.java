package com.apin.third.service.info.push;

/**
 * Created by Administrator on 2016/8/31.
 */
public class PushServiceFactory implements IPushServiceFactory{

    public static PushServiceFactory getInstance(){
        return InnerClass.instance;
    }

    static class InnerClass
    {
        private static PushServiceFactory instance=new PushServiceFactory();
    }

    @Override
    public IPushService createPushService(String osType) throws Throwable{
        if(osType.equalsIgnoreCase("3")){
            return AndriodPushService.getInstance();
        }else if(osType.equalsIgnoreCase("4")){
            return IosPushService.getInstance();
        }else{
            throw new Throwable("The osType is invalid");
        }
    }
}
