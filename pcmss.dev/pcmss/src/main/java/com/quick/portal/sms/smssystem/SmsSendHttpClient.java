package com.quick.portal.sms.smssystem;

import com.quick.portal.sms.smsServices.SmsPullSendStatusResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 短信发送httpclient，调用短信发送服务的接口并返回数据
 */
public class SmsSendHttpClient {
    //公共代码,将参数拼接之后使用get请求访问短信服务区器
    HttpClientConnectionUtil httpClientConnectionUtil = new HttpClientConnectionUtil();

    //指定模板单发
    //params 模板参数
    //sign 短信签名
    //mobile 手机号码
    //tplId 模板id
    public Object sendData(String params,String sign,String mobile,int tplId,String url){
        //单发参数是mobile，群发是mobiles
        String wholeUrl = String.format("%s?params=%s&sign=%s&mobile=%s&tplId=%d", url, params, sign,mobile,tplId);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }


    //params 模板参数
    //sign 短信签名
    //mobile 手机号码
    //tplId 模板id
    //指定模板群发 小于200的情况
    public Object smsMutSend(String params,String sign,String mobiles,int tplId,String url){
        //需要将mobiles中的手机号处理之后在发送到短信服务器中，因为短信服务器单次接收号码数量是200个
        String wholeUrl = String.format("%s?params=%s&sign=%s&mobiles=%s&tplId=%d", url, params, sign,mobiles,tplId);
        return httpClientConnectionUtil.getHttpConnection(wholeUrl);
    }

    //params 模板参数
    //sign 短信签名
    //mobile 手机号码
    //tplId 模板id
    //指定模板群发 大于200的情况
    public Object smsMutSend(String params,String sign,String[] mobiles,int tplId,String url){
        Object result = null;

        //根据数组长度，算出循环次数
        int num = mobiles.length / 200;
        //初始值
        int begin = 0;
        int end = begin+200;
        for (int i = 0; i <= num; i++) {
            //切割之后返回的字符串
            String mobilesstr = chopArray(mobiles, begin, end);
            String wholeUrl = String.format("%s?params=%s&sign=%s&mobiles=%s&tplId=%d", url, params, sign,mobilesstr,tplId);
            //分次数访问短信服务器
            result = httpClientConnectionUtil.getHttpConnection(wholeUrl);
            begin = end;
            end = begin + end;
            if(end > mobiles.length){
                end = mobiles.length;
            }
        }
        return result;

    }



    //切割数组，当手机号码数组大于200的时候将其切分，在拼接成字符串发送给短信服务器
    public String chopArray(String[] array, int begin, int end){
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = begin;i < end;i++){
            if(i == end - 1){
                stringBuilder.append(array[i]);
            }else{
                stringBuilder.append(array[i]);
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

}
