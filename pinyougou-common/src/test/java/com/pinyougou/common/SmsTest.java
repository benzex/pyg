package com.pinyougou.common;

import com.pinyougou.common.util.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用短信接口
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-04-15<p>
 */
public class SmsTest {

    public static void main(String[] args){
        HttpClientUtils httpClientUtils = new HttpClientUtils(false);
        Map<String, String> params = new HashMap<>();
        params.put("phone", "18502903967");
        params.put("signName", "五子连珠");
        params.put("templateCode", "SMS_11480310");
        params.put("templateParam", "{'number' : '8888'}");
        String content = httpClientUtils.sendPost("http://sms.pinyougou.com/sms/sendSms", params);
        System.out.println("content = " + content);
    }
}