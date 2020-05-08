package com.quick.portal.sms.smssystem;

import com.quick.portal.sms.smsServices.SmsPullSendStatusResult;
import com.quick.portal.sms.smsServices.SmsSenderUtil;
import com.quick.portal.sms.smsServices.SmsSingleSenderResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * 统计数据httpclient，调用接口返回数据
 */
public class SmsCallbackStatusHttpClient {
    //公共代码,将参数拼接之后使用get请求访问短信服务区器
    HttpClientConnectionUtil httpClientConnectionUtil = new HttpClientConnectionUtil();

    //发送数据
    public Object getSmsPullSendStatus(int stdate, int eddate, String url) {
        String wholeUrl = String.format("%s?stdate=%d&eddate=%d", url, stdate, eddate);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }

    //回执数据
    public Object getSmsCallbackStatusInfo(int stdate, int eddate, String url) {

        String wholeUrl = String.format("%s?stdate=%d&eddate=%d", url, stdate, eddate);

        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }


}
