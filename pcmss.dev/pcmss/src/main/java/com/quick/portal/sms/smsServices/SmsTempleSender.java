package com.quick.portal.sms.smsServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;



public class SmsTempleSender {
	int appid;
	String appkey;

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsTempleSender(int appid, String appkey) throws Exception {
		this.appid = appid;
		this.appkey = appkey;
	}



	/**
	 *   添加短信模板
	 *     POST https://yun.tim.qq.com/v5/tlssmssvr/add_template?sdkappid=xxxxx&random=xxxx
	 *   修改短信模板
	 *   POST https://yun.tim.qq.com/v5/tlssmssvr/mod_template?sdkappid=xxxxx&random=xxxx
	 *
	 * 添加短信模板
	 * @param remark 模板备注，比如申请原因，使用场景等
	 * @param international 0表示国内短信，1表示海外短信，默认为0
	 * @param sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @param text 模板内容
	 * @param time 请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @param title 模板名称
	 * @param type 短信类型，Enum{0：普通短信, 1：营销短信}
	 * @return {@link}SmsSignReplyResult
	 * @throws Exception
	 */
	public SmsTempleReplyResult sendTempleInfo(
			String remark,
			int international,
			String text,
			String title,
			int tpl_id,
			int type,
			String url) throws Exception {
/*
请求包体
{
    "remark": "xxxxx",
    "international": 0,
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "text": "xxxxx",
    "time": 1457336869,
    "title": "xxxxx",
    "type": 0
}
应答包体
{
    "result": 0,
    "errmsg": "",
    "data": {
        "id": 123,
        "international": 0,
        "status": 1,
        "text": "xxxxx",
        "type": 0
    }
}
*/
		// 校验 international 类型
		if (0 != international && 1 != international) {
			throw new Exception("international " + international + " error");
		}
		if (null == remark) {
			remark = "";
		}
		// 按照协议组织 post 请求包体
        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;
		JSONObject data = new JSONObject();
		data.put("remark", remark);
        data.put("international", international);
        data.put("sig", util.strToHash(String.format(
        		"appkey=%s&random=%d&time=%d",
        		appkey, random, curTime)));
		data.put("text", text);
        data.put("time", curTime);
		data.put("title", title);
		if(tpl_id >0){
			data.put("tpl_id", tpl_id);
		}
		data.put("type", type);
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
		SmsTempleReplyResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsTmplReplyResult(json);
        } else {
        	result = new SmsTempleReplyResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        
        return result;
	}



	/**
	 *   删除短信模板
	 *     POST https://yun.tim.qq.com/v5/tlssmssvr/del_template?sdkappid=xxxxx&random=xxxx
	 * @param sig App 凭证App 凭证
	 * @param tpl_id 待删除的模板 ID 数组
	 * @param time    请求发起时间，UNIX 时间戳，如果和系统时间相差超过10分钟则会返回失败
	 * @throws Exception
	 */
	public SmsRemoveReplyResult removeTempleInfo(
			ArrayList<String>  tplIds,
			String url) throws Exception {

/*
请求包体
{
     "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869,
    "tpl_id": [
        123,
        124
    ]
}
应答包体
{
    "result": 0,
    "errmsg": ""
}
*/
		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;
		JSONObject data = new JSONObject();
		data.put("sig", util.strToHash(String.format(
				"appkey=%s&random=%d&time=%d",
				appkey, random, curTime)));
		data.put("time", curTime);
		data.put("tpl_id", util.smsParamsToJSONArray(tplIds));

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
		SmsRemoveReplyResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			result = util.jsonToSmsRemoveReplyResult(json);
		} else {
			result = new SmsRemoveReplyResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}
		return result;
	}



	/**

	 *  短信模板状态查询
	 *	  POST https://yun.tim.qq.com/v5/tlssmssvr/get_sign?sdkappid=xxxxx&random=xxxx
	 * @param sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @param tpl_id 待查询的模板 ID 数组
	 * @param time 	请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @throws Exception
	 */
	public SmsTemplePullerReplyResult getTempleStatusPullerInfoByTplId(
			ArrayList<String>  tplIds,
			String url) throws Exception {

/*
请求包体
{
   {
  "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
  "time": 1457336869,
  "tpl_id": [123, 124]
}
}
应答包体
{
    "result": 0,
    "errmsg": "",
    "total": 10,
    "count": 1,
    "data": [
        {
            "id": 123,
            "international": 0,
            "reply": "xxxxx",
            "status": 0,
            "text": "xxxxx",
            "type": 0,
            "title": "xxxxx",
            "apply_time": "xxxxx",
            "reply_time": "xxxxx"
        }
    ]
}
*/
		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;
		JSONObject data = new JSONObject();
		data.put("sig", util.strToHash(String.format(
				"appkey=%s&random=%d&time=%d",
				appkey, random, curTime)));
		data.put("time", curTime);
		data.put("tpl_id", util.smsParamsToJSONArray(tplIds));
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
		SmsTemplePullerReplyResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			result = util.jsonToTemplePullerReplyResul(json);
		} else {
			result = new SmsTemplePullerReplyResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}
		return result;
	}


	/**

	 *  短信模板状态查询
	 *	  POST https://yun.tim.qq.com/v5/tlssmssvr/get_sign?sdkappid=xxxxx&random=xxxx
	 * @param sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @param tpl_page 分页查询全量模版信息，与 tpl_id 字段不能同时出现
	 * @param time 	请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @param  num 一次拉取的条数，最多50
	 * @param  stNum 拉取的偏移量，初始为0，如果要多次拉取，需赋值为上一次的 offset 与 max 字段的和
	 * @throws Exception
	 */
	public SmsTemplePullerReplyResult getTempleStatusPullerInfoByPageNo(
			int stNum,
			int num,
			String url) throws Exception {

/*
请求包体
{
  "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
  "time": 1457336869,
  "tpl_page": {
      "max": 10,
      "offset": 0
  }
}
应答包体
{
    "result": 0,
    "errmsg": "",
    "total": 10,
    "count": 1,
    "data": [
        {
            "id": 123,
            "international": 0,
            "reply": "xxxxx",
            "status": 0,
            "text": "xxxxx",
            "type": 0,
            "title": "xxxxx",
            "apply_time": "xxxxx",
            "reply_time": "xxxxx"
        }
    ]
}
*/
		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;
		JSONObject data = new JSONObject();
		JSONObject pages = new JSONObject();
		pages.put("max", num);
		pages.put("offset", stNum);

		data.put("sig", util.strToHash(String.format(
				"appkey=%s&random=%d&time=%d",
				appkey, random, curTime)));
		data.put("time", curTime);
		data.put("tpl_page", pages);
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
		SmsTemplePullerReplyResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			result = util.jsonToTemplePullerReplyResul(json);
		} else {
			result = new SmsTemplePullerReplyResult();
			result.result = httpRspCode;
			result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
		}
		return result;
	}


/*	public static void main(String[] args) throws Exception{
		SmsSignSender sms = new SmsSignSender(1400224160,
				"de43291c7169027322f84c821517182c");
		sms.send(
		0,
		"86",
		"13691186443",
		"通知：2019-06-29请到浦城数字中心开会",
		"",
		"");
	}*/


	private final static  String PIC_BASE64_PREFIX = "data:image/jpeg;base64,";
	private final static  String PIC_BASE64_SUFFIX = "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";
}
