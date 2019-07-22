package com.quick.portal.sms.smsServices;

import org.json.JSONObject;

import com.quick.portal.sms.smsServices.SmsConstants;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;


/**
 *   添加短信签名
 *
 *
 */

public class SmsSignSender {
	int appid;
	String appkey;

	SmsSenderUtil util = new SmsSenderUtil();

	public SmsSignSender(int appid, String appkey) {
		this.appid = appid;
		this.appkey = appkey;
	}



	/**
	 *   添加短信签名
	 *     POST https://yun.tim.qq.com/v5/tlssmssvr/add_sign?sdkappid=xxxxx&random=xxxx
	 *   修改短信签名
	 *   POST https://yun.tim.qq.com/v5/tlssmssvr/mod_sign?sdkappid=xxxxx&random=xxxx
	 *
	 * 普通单发短信接口，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
	 * @parm pic 签名对应的资质证明图片进行 base64 编码格式转换后的字符串
	 * base64 编码格式工具: http://base64.xpcha.com/indexie.php ，注意编译后去掉字符串前面的前缀：“data:image/jpeg;base64,”
	 * @parm international 0表示国内短信，1表示海外短信，默认为0
	 * @parm remark 签名备注，比如申请原因，使用场景等
	 * @parm sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @parm text 签名内容，不带【】，例如：【腾讯科技】这个签名，这里填"腾讯科技"
	 * @parm time 请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @return {@link}SmsSignReplyResult
	 * @throws Exception
	 */
	public SmsSignReplyResult sendSignInfo(
			String pic,
			int international,
			String remark,
			String text,
			int signId,
			String url) throws Exception {
/*
请求包体
{

    "pic": "xxxxx",
    "international": 0,
    "remark": "xxxxx",
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "text": "xxxxx",
    "time": 1457336869
}
应答包体
{
  "result": 0,
    "errmsg": "",
    "data": {
        "id": 123,
        "international": 0,
        "status": 1,
        "text": "xxxxx"
    }
}
*/

		pic = SmsConstants.PIC_BASE64_SUFFIX;

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
        data.put("pic", pic);
        data.put("international", international);
		data.put("remark", remark);
        data.put("sig", util.strToHash(String.format(
        		"appkey=%s&random=%d&time=%d",
        		appkey, random, curTime)));
        if(signId >0){
			data.put("sign_id", signId);
		}
		data.put("text", text);
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
		SmsSignReplyResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsSignReplyResult(json);
        } else {
        	result = new SmsSignReplyResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        
        return result;
	}

	/**
	 *   删除短信签名
	 *     POST https://yun.tim.qq.com/v5/tlssmssvr/del_sign?sdkappid=xxxxx&random=xxxx
	 * @parm sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @parm sign_id 签签名 ID，也可以通过值指定一个 "sign_id"：123
	 * @parm time 	请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @throws Exception
	 */
	public SmsRemoveReplyResult removeSignInfo(
			ArrayList<Integer>  signId,
			String url) throws Exception {

/*
请求包体
{
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "sign_id": [
        123,
        124
    ],
    "time": 1457336869
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
		data.put("sign_id", util.paramsToJSONArray(signId));
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

	 *  查询申请的短信签名状态
	 *	  POST https://yun.tim.qq.com/v5/tlssmssvr/get_sign?sdkappid=xxxxx&random=xxxx
	 * @parm sig App 凭证，"sig" 字段根据公式 sha256（appkey=$appkey&random=$random&time=$time）
	 * @parm sign_id 签签名 ID，也可以通过值指定一个 "sign_id"：123
	 * @parm time 	请求发起时间，UNIX 时间戳，如果和系统时间相差超过 10 分钟则会返回失败
	 * @throws Exception
	 */
	public SmsSignPullerReplyResult getSignStatusPullerInfo(
			ArrayList<Integer>  signId,
			String url) throws Exception {

/*
请求包体
{
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "sign_id": [
        123,
        124
    ],
    "time": 1457336869
}
应答包体
{ "result": 0,
    "errmsg": "",
    "count": 3,
    "data": [{
        "id": 123,
        "international": 0,
        "reply": "xxxxx",
        "status": 0,
        "text": "xxxxx",
        "apply_time": "2018-04-29 10:34:55",
        "reply_time": "2018-04-29 10:39:55"
    }]
}
*/
		// 按照协议组织 post 请求包体
		long random = util.getRandom();
		long curTime = System.currentTimeMillis()/1000;
		JSONObject data = new JSONObject();

		data.put("sig", util.strToHash(String.format(
				"appkey=%s&random=%d&time=%d",
				appkey, random, curTime)));
		data.put("sign_id", util.paramsToJSONArray(signId));
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
		SmsSignPullerReplyResult result;
		if (httpRspCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			JSONObject json = new JSONObject(sb.toString());
			result = util.jsonToSignPullerReplyResul(json);
		} else {
			result = new SmsSignPullerReplyResult();
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


}
