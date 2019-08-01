package com.quick.portal.sms.smsServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;


/**
 * 发送数据统计
 *
 *
 */
public class SmsPullSendStatus {

	int appid;
	String appkey;

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsPullSendStatus(int appid, String appkey) {
		this.appid = appid;
		this.appkey = appkey;
	}

	/**
	 *   拉取一段时间内的短信发送状态，包括发送量、成功量以及计费条数。
	 * @param bdate 开始时间，yyyymmddhh 需要拉取的起始时间，精确到小时
	 * @param edate  结束时间，yyyymmddhh 需要拉取的截止时间，精确到小时
	 * @return
	 * @throws Exception
	 */
	public HttpURLConnection getConConnection(int bdate,int edate) throws Exception{
		/*
		{
    	"begin_date": 2016090800,
    	"end_date": 2016090823,
   	 	"sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    	"time": 1457336869
		}
		*/
		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis() / 1000;

		JSONObject data = new JSONObject();
		data.put("sig", util.strToHash(String.format(
				"appkey=%s&random=%d&time=%d", appkey, random,curTime)));
		data.put("time", curTime);
		data.put("begin_date", bdate);
		data.put("end_date", edate);
		// 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", SmsConstants.PULL_SEND_STATUS_URL, appid, random);
		HttpURLConnection conn = util.getPostHttpConn(wholeUrl);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
		wr.write(data.toString());
		wr.flush();
		return conn;
	}
	/**
	 * 拉取一段时间内的短信发送状态，包括发送量、成功量以及计费条数。
	 *
	 * @param begin_date 开始时间，yyyymmddhh 需要拉取的起始时间，精确到小时
	 * @param end_date  结束时间，yyyymmddhh 需要拉取的截止时间，精确到小时
	 *
	 *
	 * @return {@link}pullCallback
	 * @throws Exception
	 */
	public SmsPullSendStatusResult pullSendStatus(int begin_date,int end_date) throws Exception {
/**
 *
 *
 * {
 * 			"begin_date": 2016090800,
 * 				"end_date": 2016090823,
 * 				"sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
 * 				"time": 1457336869
 *                }
 */


		HttpURLConnection conn = getConConnection(begin_date,end_date);//1表示短信回执
		// 显示 POST 请求返回的内容
		StringBuilder sb = new StringBuilder();
		int httpRspCode = conn.getResponseCode();
		SmsPullSendStatusResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			System.out.println(sb.toString());
			result = util.jsonToSmsPullSendStatusResult(json);
		} else {
			result = new SmsPullSendStatusResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}

		return result;
	}
	
	


}
