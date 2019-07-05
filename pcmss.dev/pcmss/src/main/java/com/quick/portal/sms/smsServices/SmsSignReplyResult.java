package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsSignReplyResult {
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

		public String toString(){
			return String.format(
					"id:%d\t"
					+"international:%d\t"
					+"status:%d\t"
					+"text:%s\n",
					id,
					international,
					status,
					text
			  );
		}
	}
	
	public  int result;
	public String errmsg;
	public ArrayList<data> datas;
	
	public String toString() {
		if (0 == result) {
			return String.format("SmsSignReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n"
							+ "datas:%s\n",
					result,
					errmsg,
					datas.toString()
			);
		} else {
			return String.format("SmsSignReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n", result, errmsg);
		}
	}
}

