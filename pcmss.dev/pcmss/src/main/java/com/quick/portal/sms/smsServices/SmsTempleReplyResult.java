package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsTempleReplyResult {
	/*
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
	public class data {
		public int id;
		public int international;
		public int status;
		public String text;
		public int type;
	}

	public int result;
	public String errmsg;
	public Object data;
	


		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	}

