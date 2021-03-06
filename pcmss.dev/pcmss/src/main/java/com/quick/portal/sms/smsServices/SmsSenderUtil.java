package com.quick.portal.sms.smsServices;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import com.quick.portal.sms.mouldmng.IMouldMngDao;
import com.quick.portal.sms.mouldmng.MouldMngDO;
import com.quick.portal.sms.mouldmng.MouldMngServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;

public class SmsSenderUtil {

    protected Random random = new Random();
    
    public String stringMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] inputByteArray = input.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }
    
    protected String strToHash(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] inputByteArray = str.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    public String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }
    
    public int getRandom() {
    	return random.nextInt(999999)%900000+100000;
    }
    
    public HttpURLConnection getPostHttpConn(String url) throws Exception {
        URL object = new URL(url);
        HttpURLConnection conn;
        conn = (HttpURLConnection) object.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");
        return conn;
	}
    
    public String calculateSig(
    		String appkey,
    		long random,
    		String msg,
    		long curTime,    		
    		ArrayList<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		appkey, random, curTime, phoneNumbersString));
    }
    
    public String calculateSigForTempl(
    		String appkey,
    		long random,
    		long curTime,    		
    		ArrayList<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		appkey, random, curTime, phoneNumbersString));
    }
    
    public String calculateSigForTempl(
    		String appkey,
    		long random,
    		long curTime,    		
    		String phoneNumber) throws NoSuchAlgorithmException {
    	ArrayList<String> phoneNumbers = new ArrayList<>();
    	phoneNumbers.add(phoneNumber);
    	return calculateSigForTempl(appkey, random, curTime, phoneNumbers);
    }
    
    public JSONArray phoneNumbersToJSONArray(String nationCode, ArrayList<String> phoneNumbers) {
        JSONArray tel = new JSONArray();
        int i = 0;
        do {
            JSONObject telElement = new JSONObject();
            telElement.put("nationcode", nationCode);
            telElement.put("mobile", phoneNumbers.get(i));
            tel.put(telElement);
        } while (++i < phoneNumbers.size());

        return tel;
    }

	/**
	 * params": [
	 * 			{"1234",
	 * 	         "4"
	 * 	         }
	 * 		{"1234",
	 * 	      "4"}

	 *     ],
	 *
	 * @param params
	 * @return
	 */
	public JSONArray smsParamsToJSONArray(List<String> params) {
        JSONArray smsParams = new JSONArray();
		String param = "";
        for (int i = 0; i < params.size(); i++) {
			param = params.get(i);
			String [] pas = param.split(",");
			for(int j=0; j<pas.length;j++){
				smsParams.put(pas[j]);
			}
		}
        return smsParams;
    }

	public JSONArray pmsToJSONArray(List<String> params) {
		JSONArray smsParams = new JSONArray();
		for (int i = 0; i < params.size(); i++) {
			smsParams.put(params.get(i));
		}
		return smsParams;
	}

	public JSONArray paramsToJSONArray(List<Integer> params) {
		JSONArray smsParams = new JSONArray();
		for (int i = 0; i < params.size(); i++) {
			smsParams.put(params.get(i));
		}
		return smsParams;
	}
    
    public SmsSingleSenderResult jsonToSmsSingleSenderResult(JSONObject json) {
    	SmsSingleSenderResult result = new SmsSingleSenderResult();
    	result.result = json.getInt("result");
    	result.errmsg = json.getString("errmsg");
		if("send success".equals(result.errmsg)){
			if (0 == result.result) {
				result.ext = json.getString("ext");
				result.sid = json.getString("sid");
				result.fee = json.getInt("fee");
			}
		}
    	return result;
    }
    
    public SmsMultiSenderResult jsonToSmsMultiSenderResult(JSONObject json) {
    	SmsMultiSenderResult result = new SmsMultiSenderResult();
    	
    	result.result = json.getInt("result");
    	result.errmsg = json.getString("errmsg");
    	if (false == json.isNull("ext")) {
    		result.ext = json.getString("ext");	
    	}
    	if (0 != result.result) {
    		return result;
    	}
    	
    	result.details = new ArrayList<>();    	
    	JSONArray details = json.getJSONArray("detail");
    	for (int i = 0; i < details.length(); i++) {
    		JSONObject jsonDetail = details.getJSONObject(i);
    		SmsMultiSenderResult.Detail detail = result.new Detail();
    		detail.result = jsonDetail.getInt("result");
    		detail.errMsg = jsonDetail.getString("errmsg");
			detail.phoneNumber = jsonDetail.getString("mobile");
			detail.nationCode = jsonDetail.getString("nationcode");
    		if (0 == detail.result) {
	    		if (false == jsonDetail.isNull("sid")) {
	    			detail.sid = jsonDetail.getString("sid");	
	    		}		
	    		detail.fee = jsonDetail.getInt("fee");
    		}
    		result.details.add(detail);
    	}
    	return result;
    }
    

    public SmsStatusPullCallbackResult jsonToSmsStatusPullCallbackrResult(JSONObject json) {
    	SmsStatusPullCallbackResult result = new SmsStatusPullCallbackResult();
    	
    	result.result = json.getInt("result");
    	result.errmsg = json.getString("errmsg");    	
    	if (true == json.isNull("data")) {
    		return result;
    	}
    	result.callbacks = new ArrayList<>();  
    	JSONArray datas  = json.getJSONArray("data");
    	for(int index = 0 ; index< datas.length(); index++){
    			JSONObject cb = datas.getJSONObject(index);
    			SmsStatusPullCallbackResult.Callback callback = result.new Callback();
    			callback.user_receive_time = cb.getString("user_receive_time");
    			callback.nationcode = cb.getString("nationcode");
    			callback.mobile = cb.getString("mobile");
    			callback.report_status = cb.getString("report_status");
    			callback.errmsg = cb.getString("errmsg");
    			callback.description = cb.getString("description");
    			callback.sid = cb.getString("sid");
    			result.callbacks.add(callback);
    	}
    	return result;
    }

    public SmsStatusPullReplyResult jsonToSmsStatusPullReplyResult(JSONObject json) {
    	SmsStatusPullReplyResult result = new SmsStatusPullReplyResult();
    	
    	result.result = json.getInt("result");
    	result.errmsg = json.getString("errmsg");
    	if (0 == result.result) {
			result.count = json.getInt("count");
		}
    	
    	if (true == json.isNull("data")) {
    		return result;
    	}
    	
    	result.replys = new ArrayList<>();  
    	JSONArray datas  = json.getJSONArray("data");
    	for(int index = 0 ; index< datas.length(); index++){
    			JSONObject reply_json = datas.getJSONObject(index);
    			SmsStatusPullReplyResult.Reply reply = result.new Reply();
    			reply.nationcode = reply_json.getString("nationcode");
    		  	reply.mobile = reply_json.getString("mobile");
    		  	reply.sign = reply_json.getString("sign");
    	    	reply.text = reply_json.getString("text"); 
    	    	reply.time = reply_json.getLong("time"); 
    			result.replys.add(reply);
    	}
    	
    	return result;
    }

	public SmsSignReplyResult jsonToSmsSignReplyResult(JSONObject json) {
		SmsSignReplyResult result = new SmsSignReplyResult();
		result.result = json.getInt("result");
		if(result.result >0){
			result.errmsg = json.getString("msg");
			return result;
		}
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("data")) {
			return result;
		}
		JSONObject datas = json.getJSONObject("data");
		SmsSignReplyResult.data reply = result.new data();
		reply.id = datas.getInt("id");
		reply.international = datas.getInt("international");
		reply.status = datas.getInt("status");
		reply.text = datas.getString("text");
		result.setData(reply);
		return result;
	}

	public SmsRemoveReplyResult jsonToSmsRemoveReplyResult(JSONObject json) {
		SmsRemoveReplyResult result = new SmsRemoveReplyResult();
		result.result = json.getInt("result");
		if(result.result >0){
			result.errmsg = json.getString("msg");
			return result;
		}
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("data")) {
			return result;
		}
		return result;
	}


	public SmsSignPullerReplyResult jsonToSmsSignPullerReplyResult(JSONObject json) {
		SmsSignPullerReplyResult result = new SmsSignPullerReplyResult();
		result.result = json.getInt("result");
		if(result.result >0){
			result.errmsg = json.getString("msg");
			return result;
		}
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("datas")) {
			return result;
		}
		result.datas = new ArrayList<>();
		JSONArray datas  = json.getJSONArray("datas");
		result.count = datas.length();
		for(int index = 0 ; index< datas.length(); index++){
			JSONObject reply_json = datas.getJSONObject(index);
			SmsSignPullerReplyResult.data reply = result.new data();
			reply.id = reply_json.getInt("id");
			reply.international = reply_json.getInt("international");
			reply.reply = reply_json.getString("reply");
			reply.status = reply_json.getInt("status");
			reply.text = reply_json.getString("text");
			reply.apply_time = reply_json.getString("apply_time");
			//reply.reply_time = reply_json.getString("reply_time");
			reply.reply_time = reply.apply_time;
			result.datas.add(reply);
		}
		return result;
	}


	public SmsTemplePullerReplyResult jsonToTemplePullerReplyResult(JSONObject json) {
		SmsTemplePullerReplyResult result = new SmsTemplePullerReplyResult();
		result.result = json.getInt("result");
		if(result.result >0){
			result.errmsg = json.getString("msg");
			return result;
		}
		result.errmsg = json.getString("errmsg");

		if (true == json.isNull("datas")) {
			return result;
		}
		result.datas = new ArrayList<>();
		JSONArray datas  = json.getJSONArray("datas");
		result.count = datas.length();
		for(int index = 0 ; index< datas.length(); index++){
			JSONObject reply_json = datas.getJSONObject(index);
			SmsTemplePullerReplyResult.data reply = result.new data();
			reply.id = reply_json.getInt("id");
			//获取短信类型属性
			//reply.type = service.getSmsType(reply_json.getInt("id"));

			reply.international = reply_json.getInt("international");
			reply.reply  = reply_json.getString("reply");
			reply.status = reply_json.getInt("status");
			//reply.text = reply_json.getString("text");
			reply.title  = reply_json.getString("title");
			reply.apply_time = reply_json.getString("apply_time");
			reply.reply_time = reply.apply_time;
			result.datas.add(reply);
		}
		return result;
	}

	/**
	 * {
	 *     "result": 0,
	 *     "errmsg": "",
	 *     "data": {
	 *         "id": 123,
	 *         "international": 0,
	 *         "status": 1,
	 *         "text": "xxxxx",
	 *         "type": 0
	 *     }
	 * }
	 *
	 */
	public SmsTempleReplyResult jsonToSmsTmplReplyResult(JSONObject json) {
		SmsTempleReplyResult result = new SmsTempleReplyResult();
		result.result = json.getInt("result");
		if(result.result >0){
			result.errmsg = json.getString("msg");
			return result;
		}
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("data")) {
			return result;
		}
		JSONObject datas = json.getJSONObject("data");
		SmsTempleReplyResult.data reply = result.new data();
		reply.id = datas.getInt("id");
		reply.international = datas.getInt("international");
		reply.status = datas.getInt("status");
		reply.text = datas.getString("text");
		reply.type = datas.getInt("type");
		result.setData(reply);
		return result;
	}


	public SmsPullCallbackStatusReplyResult jsonToSmsPullCallbackStatusReplyResult(JSONObject json) {
		SmsPullCallbackStatusReplyResult result = new SmsPullCallbackStatusReplyResult();
		result.result = json.getInt("result");
		if(result.result >0){
			result.errmsg = json.getString("msg");
			return result;
		}
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("data")) {
			return result;
		}
		JSONObject data  = json.getJSONObject("data");
		SmsPullCallbackStatusReplyResult.data reply = result.new data();
		reply.status = data.getLong("status");
		reply.status_fail = data.getLong("status_fail");
		reply.status_fail_0 = data.getLong("status_fail_0");
		reply.status_fail_1 = data.getLong("status_fail_1");
		reply.status_fail_2 = data.getLong("status_fail_2");
		reply.status_fail_3 = data.getLong("status_fail_3");
		reply.status_fail_4 = data.getLong("status_fail_4");
		reply.status_success = data.getLong("status_success");
		reply.success = data.getLong("success");
		result.setData(reply);
		return result;
	}

	public SmsStatusPullerResult jsonToSmsStatusPullerResult(JSONObject json) {
		SmsStatusPullerResult result = new SmsStatusPullerResult();
		result.result = json.getInt("result");
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("data")) {
			return result;
		}
		JSONObject datas  = json.getJSONObject("data");
		SmsStatusPullerResult.Data reply = result.new Data();
		reply.status = datas.getLong("status");
		reply.status_fail = datas.getLong("status_fail");
		reply.status_fail_0 = datas.getLong("status_fail_0");
		reply.status_fail_1 = datas.getLong("status_fail_1");
		reply.status_fail_2 = datas.getLong("status_fail_2");
		reply.status_fail_3 = datas.getLong("status_fail_3");
		reply.status_fail_4 = datas.getLong("status_fail_4");
		reply.status_success = datas.getLong("status_success");
		reply.success = datas.getLong("success");

	/*	for(int index = 0 ; index< datas.length(); index++){
			JSONObject reply_json = datas.getJSONObject(index);
			SmsPullCallbackStatusReplyResult.data reply = result.new data();
			reply.status = reply_json.getLong("status");
			reply.status_fail = reply_json.getLong("status_fail");
			reply.status_fail_0 = reply_json.getLong("status_fail_0");
			reply.status_fail_1 = reply_json.getLong("status_fail_1");
			reply.status_fail_2 = reply_json.getLong("status_fail_2");
			reply.status_fail_3 = reply_json.getLong("status_fail_3");
			reply.status_fail_4 = reply_json.getLong("status_fail_4");
			reply.status_success = reply_json.getLong("status_success");
			reply.success = reply_json.getLong("success");
			result.datas.add(reply);
		}*/
		return result;
	}

	public SmsPullSendStatusResult jsonToSmsPullSendStatusResult(JSONObject json) {
		SmsPullSendStatusResult result = new SmsPullSendStatusResult();
		result.result = json.getInt("result");
		result.errmsg = json.getString("errmsg");
		if (true == json.isNull("data")) {
			return result;
		}
		JSONObject datas  = json.getJSONObject("data");
		SmsPullSendStatusResult.Data reply = result.new Data();
		reply.bill_number = datas.getLong("bill_number");
		reply.request = datas.getLong("request");
		reply.success = datas.getLong("success");
		result.setData(reply);


	/*	for(int index = 0 ; index< datas.length(); index++){
			JSONObject reply_json = datas.getJSONObject(index);
			SmsPullCallbackStatusReplyResult.data reply = result.new data();
			reply.status = reply_json.getLong("status");
			reply.status_fail = reply_json.getLong("status_fail");
			reply.status_fail_0 = reply_json.getLong("status_fail_0");
			reply.status_fail_1 = reply_json.getLong("status_fail_1");
			reply.status_fail_2 = reply_json.getLong("status_fail_2");
			reply.status_fail_3 = reply_json.getLong("status_fail_3");
			reply.status_fail_4 = reply_json.getLong("status_fail_4");
			reply.status_success = reply_json.getLong("status_success");
			reply.success = reply_json.getLong("success");
			result.datas.add(reply);
		}*/
		return result;
	}

}
