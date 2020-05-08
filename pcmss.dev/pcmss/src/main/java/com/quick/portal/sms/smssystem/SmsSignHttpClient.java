package com.quick.portal.sms.smssystem;

import java.net.URLEncoder;

public class SmsSignHttpClient {
    HttpClientConnectionUtil httpClientConnectionUtil = new HttpClientConnectionUtil();
    //添加签名
    public Object addSign(String pic,String text,String remark,String url){
        //不添加pic 参数，使用默认的。图片base码，后续有需要请联系管理员
        String wholeUrl = String.format("%s?text=%s&remark=%s", url, text, remark);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }
    //修改签名
    public Object modSign(String pic,int signId,String text,String remark,String url){
        String wholeUrl = String.format("%s?pic=%s&signId=%s&text=%s&remark=%s", url, pic,String.valueOf(signId) ,text, remark);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }
    //删除签名
    public Object delSign(String signIds ,String url){
        String wholeUrl = String.format("%s?signIds=%s", url,signIds);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }
    //查询签名
    public Object getSign(String signIds ,String url){
        String wholeUrl = String.format("%s?signIds=%s", url,signIds);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }
}
