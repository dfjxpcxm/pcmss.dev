package com.quick.portal.sms.smsServices;

import com.quick.core.util.common.DateTime;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;

/*
 * 回执数据统计
 */

public class SearchSmsPullCallbackStatusResult {
	int appid;
	String appkey;
	SmsSenderUtil util = new SmsSenderUtil();

	public SearchSmsPullCallbackStatusResult(int appid, String appkey) throws Exception {
		this.appid = appid;
		this.appkey = appkey;
	}



	/**
	 *   回执数据统计
	 *     POST https://yun.tim.qq.com/v5/tlssmssvr/pullcallbackstatus?sdkappid=xxxxx&random=xxxx
	 * @param begin_date 开始时间，yyyymmddhh 需要拉取的起始时间,精确到小时
	 * @param end_date 结束时间，yyyymmddhh 需要拉取的截止时间,精确到小时
	 * @param sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @param time 请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @return {@link}SmsSignReplyResult
	 * @throws Exception
	 */
	public SmsPullCallbackStatusReplyResult getCallbackStatusResult(
			String begin_date,
			String end_date,
			String url) throws Exception {
/*
请求包体
{
    "begin_date": 2016090800,
    "end_date": 2016090823,
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869

}
应答包体
{
    "data": {
        "status": 90,
        "status_fail": 10,
        "status_fail_0": 2,
        "status_fail_1": 2,
        "status_fail_2": 2,
        "status_fail_3": 2,
        "status_fail_4": 2,
        "status_success": 80,
        "success": 100
    },
    "errmsg": "OK",
    "result": 0
}
*/

		// 校验 begin_date 类型
		DateTime sdt = new DateTime();
		if (null == begin_date || "".equals(begin_date) ) {
			throw new Exception("begin_date " + begin_date + " error");
		}else{
			sdt = DateTime.parseExact(begin_date, "yyyyMMdd HH");
		}
		DateTime edt = new DateTime();
		if (null == end_date || "".equals(end_date) ) {
			throw new Exception("end_date " + end_date + " error");
		}else{
			edt = DateTime.parseExact(end_date, "yyyyMMdd HH");
		}

		// 按照协议组织 post 请求包体
        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;
		JSONObject data = new JSONObject();
        data.put("begin_date", sdt);
        data.put("end_date", edt);
        data.put("sig", util.strToHash(String.format(
        		"appkey=%s&random=%d&time=%d",
        		appkey, random, curTime)));
        data.put("time", curTime);

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", url, appid, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();
        System.out.println(data.toString());
        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
		SmsPullCallbackStatusReplyResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsPullCallbackStatusReplyResult(json);
        } else {
        	result = new SmsPullCallbackStatusReplyResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        
        return result;
	}


}
