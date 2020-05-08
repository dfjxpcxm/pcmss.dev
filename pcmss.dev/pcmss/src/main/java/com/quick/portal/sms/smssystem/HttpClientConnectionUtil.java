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
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientConnectionUtil {

    public Object getHttpConnection(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        SmsPullSendStatusResult result = new SmsPullSendStatusResult();
        try {

            HttpGet httpget = new HttpGet(url);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    //响应状态码
                    HttpEntity entity = response.getEntity();
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        //封装错误信息,请求接口时报错信息
                        result.result = status;
                        result.errmsg = "http error " + status + " " + response.toString();
                        return result.toString();
                        //throw new ClientProtocolException("Unexpected response status: " + status);
                    }

                }
            };
            //返回回服务器请求的数据，即返回handleResponse中的return 语句
            return httpclient.execute(httpget, responseHandler);

        } catch (IOException e) {
            //门户系统报错信息
            e.printStackTrace();
            result.result = 9999;
            result.errmsg = e.getMessage();
            return result.toString();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
