package com.apin.third.service.sms.and.email;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public interface INotifyService {

    public void notify(String title,String body,String... user);

}
