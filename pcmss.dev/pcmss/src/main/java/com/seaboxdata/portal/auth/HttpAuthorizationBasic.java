package com.seaboxdata.portal.auth;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;
import java.util.Map;



import com.alibaba.fastjson.JSONObject;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsStatusPullCallbackResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.xml.bind.DatatypeConverter;


import net.sf.json.JSONArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class HttpAuthorizationBasic {

    /**

     * 向指定URL发送GET方法的请求

     *

     * @param url

     *            发送请求的URL

     * @param param

     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。

     * @return URL 所代表远程资源的响应结果

     */

    public static String sendGet(String url, String param) {

        String result = "";
        BufferedReader in = null;

        try {

            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {

                System.out.println(key + "--->" + map.get(key));

            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;

    }



    /**

     * 向指定 URL 发送POST方法的请求

     *

     * @param url

     *            发送请求的 URL

     * @param param

     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。

     * @return 所代表远程资源的响应结果

     */

    public static String sendPost(String url, JSONObject param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public int httpClientWithBasicAuth(String username, String password, String uri, Map<String, String> paramMap) {
        try {
            // 创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            HttpPost httpPost = new HttpPost(uri);
            //添加http头信息
            httpPost.addHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((username + ":" + password).getBytes()));

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            paramMap.forEach((k,v)->{
                builder.addPart(k, new StringBody(v, ContentType.MULTIPART_FORM_DATA));
            });
            HttpEntity postEntity = builder.build();
            httpPost.setEntity(postEntity);
            String result = "";
            HttpResponse httpResponse = null;
            HttpEntity entity = null;
            try {
                httpResponse = closeableHttpClient.execute(httpPost);
                entity = httpResponse.getEntity();
                if( entity != null ){
                    result = EntityUtils.toString(entity);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 关闭连接
            closeableHttpClient.close();

            System.out.println(result);
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return 0;
    }





    /**
     * Created by xenia on 2017/6/6.
     * http://localhost:8080/pcms/
     */

        public static void main(String[] args) throws ClientProtocolException, IOException {
            //认证信息对象，用于包含访问翻译服务的用户名和密码

//            String path = "http://106.52.132.19:8080/pcmss/mobile/login";
            String path = "http://localhost:8080/pcmss/mobile/login";
            //1.创建客户端访问服务器的httpclient对象   打开浏览器
            HttpClient httpclient = new DefaultHttpClient();
            //2.以请求的连接地址创建get请求对象     浏览器中输入网址
            HttpGet httpget = new HttpGet(path);

            //username:password--->访问的用户名，密码,并使用base64进行加密，将加密的字节信息转化为string类型，encoding--->token
//            String encoding = DatatypeConverter.printBase64Binary("username:password".getBytes("UTF-8"));
            String encoding = DatatypeConverter.printBase64Binary("admin:admin".getBytes("UTF-8"));

            httpget.setHeader("Authorization", "Basic " +encoding);
            //3.向服务器端发送请求 并且获取响应对象  浏览器中输入网址点击回车
            HttpResponse response = httpclient.execute(httpget);
            //4.获取响应对象中的响应码
            StatusLine statusLine = response.getStatusLine();//获取请求对象中的响应行对象
            int responseCode = statusLine.getStatusCode();//从状态行中获取状态码

            System.out.println(responseCode);
            if (responseCode == 200) {
                //5.  可以接收和发送消息
                HttpEntity entity = response.getEntity();
                //6.从消息载体对象中获取操作的读取流对象
                InputStream input = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String str1 = br.readLine();
                String result = new String(str1.getBytes("gbk"), "utf-8");
                System.out.println("服务器的响应是:" + result);
                br.close();
                input.close();
            } else {
                System.out.println("响应失败!");
            }
        }
    }

