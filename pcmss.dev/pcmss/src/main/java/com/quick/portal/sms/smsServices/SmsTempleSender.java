package com.quick.portal.sms.smsServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


public class SmsTempleSender {
	int appid;
	String appkey;

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsTempleSender(int appid, String appkey) {
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
	 * @parm remark 模板备注，比如申请原因，使用场景等
	 * @parm international 0表示国内短信，1表示海外短信，默认为0
	 * @parm sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @parm text 模板内容
	 * @parm time 请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @parm title 模板名称
	 * @parm type 短信类型，Enum{0：普通短信, 1：营销短信}
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
	 * @parm sig App 凭证App 凭证
	 * @parm tpl_id 待删除的模板 ID 数组
	 * @parm time    请求发起时间，UNIX 时间戳，如果和系统时间相差超过10分钟则会返回失败
	 * @throws Exception
	 */
	public SmsRemoveReplyResult removeTempleInfo(
			List<String> tplIds,
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
	 * @parm sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @parm tpl_id 待查询的模板 ID 数组
	 * @parm time 	请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
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
	 * @parm sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @parm tpl_page 分页查询全量模版信息，与 tpl_id 字段不能同时出现
	 * @parm time 	请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @parm  num 一次拉取的条数，最多50
	 * @parm  stNum 拉取的偏移量，初始为0，如果要多次拉取，需赋值为上一次的 offset 与 max 字段的和
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

}
