package com.quick.portal.sms.smsServices;



public class SmsSignReplyResult {
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/*
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
	public class data{
		public int id;
		public int international;
		public int status;
		public String text;

	}
	
	public  int result;
	public String errmsg;
	public Object data;
	

}

