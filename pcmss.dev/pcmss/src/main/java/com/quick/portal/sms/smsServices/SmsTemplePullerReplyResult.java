package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsTemplePullerReplyResult {
/*


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
}
 */
	public class data {
		public int id;
		public int international;
		public String reply = "";
		public int status;
		public String text = "";
		public int type;
		public String title = "";
		public String apply_time = "";
		public String reply_time = "";


		
		public String toString() {
			if (0 == result) {
				return String.format(
						"data id %d\ninternational %d\nreply %s\nstatus %d\ntext %s\ntype %d\ntitle %s\napply_time %s\nreply_time %s",
						id,international, reply, status, text, type,title,apply_time, reply_time);
			} else {
				return String.format(
						"data id %d\ninternational %d\nreply %s\nstatus %d\ntext %s\ntype %d\ntitle %s\napply_time %s\nreply_time %s",
						id,international, reply, status, text, type,title,apply_time, reply_time);
			}
		}
	}

	public int result;
	public String errmsg = "";
	public int count ;
	public ArrayList<data> datas;
	
	public String toString() {
		return String.format(
				"SmsSignPullerReplyResult\nresult %d\nerrmsg %s\ncount %d\nddatas %s",
				result, errmsg, count, datas);
	}
}
