package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsSignPullerReplyResult {
/*


    "result": 0,
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
	public class data {
		public int id;
		public int international;
		public String reply = "";
		public int status;
		public String text = "";
		public String apply_time = "";
		public String reply_time = "";


		
		public String toString() {
			if (0 == result) {
				return String.format(
						"data id %d\ninternational %d\nreply %s\nstatus %d\ntext %s\napply_time %s\nreply_time %s",
						id,international, reply, status, text, apply_time, reply_time);
			} else {
				return String.format(
						"data id %d\ninternational %d\nreply %s\nstatus %d\ntext %s\napply_time %s\nreply_time %s",
						id,international, reply, status, text, apply_time, reply_time);
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
