package com.apin.third.service.sms.and.email;

import com.apin.utils.Constant;
import org.junit.After;
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
 * Created by Administrator on 2016/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:testSms.xml")
public class SmsNotifyServiceTest {

    @Resource
    private SmsNotifyService smsNotifyService;

    @Before
    public void setUp() throws Exception {
        Constant.DICT_SMS_MODEL_MAP.put("testSms","【爱拼机】#user#[发起行程:#departToPlace#，有效时间:#date#]，请前往APP查看");
    }

    @After
    public void tearDown() throws Exception {
        Constant.DICT_SMS_MODEL_MAP.clear();
    }

    @Test
    public void testSmsModelNotify() throws Exception {
        List<String> params=new ArrayList<>();
        params.add("山本大将");
        params.add("（越南-->大阪）");
        params.add("2016-09-18 18:46");
        List<String> phones=new ArrayList<>();
        phones.add("18817862033");
        phones.add("18626052087");
        smsNotifyService.smsModelNotify("testSms", "测试SMS", params.toString(), phones.toString());
        Thread.sleep(20000);
    }

}