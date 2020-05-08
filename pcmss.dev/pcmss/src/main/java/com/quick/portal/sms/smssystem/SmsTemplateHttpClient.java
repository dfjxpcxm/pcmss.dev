package com.quick.portal.sms.smssystem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SmsTemplateHttpClient {

    //公共代码,将参数拼接之后使用get请求访问短信服务区器
    HttpClientConnectionUtil httpClientConnectionUtil = new HttpClientConnectionUtil();

    //添加模板
    public Object addTemplate(String remark, String text, String title, int type, String url) throws UnsupportedEncodingException {
        String wholeUrl = String.format("%s?title=%s&type=%s&remark=%s&text=%s", url, title, String.valueOf(type), remark,URLEncoder.encode(text, "utf-8"));
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }

    //删除模板
    public Object delTemplate(String tplIds,String url) {
        String wholeUrl = String.format("%s?tplIds=%s", url, tplIds);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }

    //修改模板
    public Object modTemplate(String remark, Integer tplId, String text, String title, int type, String url) throws UnsupportedEncodingException {
        String wholeUrl = String.format("%s?title=%s&tplId=%s&type=%s&remark=%s&text=%s", url, title, String.valueOf(tplId),String.valueOf(type), remark, URLEncoder.encode(text, "utf-8"));
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }

    //查询模板
    public Object getTemplateInfoById(String tplIds,String url) {
        String wholeUrl = String.format("%s?tplIds=%s", url, tplIds);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }


}
