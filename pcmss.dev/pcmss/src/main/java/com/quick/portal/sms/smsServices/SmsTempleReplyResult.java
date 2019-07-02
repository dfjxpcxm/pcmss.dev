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
	public class data{
		int id;
		int international;
		int status;
		String text;
		int type;

		public String toString(){
			return String.format(
					"id:%d\t"
					+"international:%d\t"
					+"status:%d\t"
					+"text:%s\t"
					+"type:%s\n",
					id,
					international,
					status,
					text,
					type
			  );
		}
	}
	
	int result;
	String errmsg;
	ArrayList<data> datas;
	
	public String toString() {
		if (0 == result) {
			return String.format("SmsStatusReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n"
							+ "datas:%s\n",
					result,
					errmsg,
					datas.toString()
			);
		} else {
			return String.format("SmsStatusReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n", result, errmsg);
		}
	}
}

