package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsPullCallbackStatusReplyResult implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
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
		public long status ;
		public long status_fail;
		public long status_fail_0;
		public long status_fail_1;
		public long status_fail_2;
		public long status_fail_3;
		public long status_fail_4;
		public long status_success ;
		public long success ;
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

