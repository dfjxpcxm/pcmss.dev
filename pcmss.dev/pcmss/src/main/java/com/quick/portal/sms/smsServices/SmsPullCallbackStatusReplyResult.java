package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsPullCallbackStatusReplyResult {
	/*
{
    "data": {
        "status": 90,
        "status_fail": 10,
        "status_fail_0": 2,
        "status_fail_1": 2,
        "status_fail_2": 2,
        "status_fail_3": 2,
        "status_fail_4": 2,
        "status_success": 80,
        "success": 100
    },
    "errmsg": "OK",
    "result": 0
}
	 */
	public class data{
		long status ;
		long status_fail;
		long status_fail_0;
		long status_fail_1;
		long status_fail_2;
		long status_fail_3;
		long status_fail_4;
		long status_success ;
		long success ;
		public String toString(){
			return String.format(
					"status:%d\t"
					+"status_fail:%d\t"
					+"status_fail_0:%d\t"
					+"status_fail_1:%d\t"
					+"status_fail_2:%d\t"
					+"status_fail_3:%d\t"
					+"status_fail_4:%d\t"
					+"status_success:%d\t"
					+"success:%d\n",
					status,
					status_fail,
					status_fail_0,
					status_fail_1,
					status_fail_2,
					status_fail_3,
					status_fail_4,
					status_success,
					success
			  );
		}
	}
	
	int result;
	String errmsg;
	ArrayList<data> datas;
	
	public String toString() {
		if (0 == result) {
			return String.format("SmsPullCallbackStatusReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n"
							+ "datas:%s\n",
					result,
					errmsg,
					datas.toString()
			);
		} else {
			return String.format("SmsPullCallbackStatusReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n", result, errmsg);
		}
	}
}

