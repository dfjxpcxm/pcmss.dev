package com.quick.portal.sms.smsServices;

import java.util.ArrayList;

public class SmsRemoveReplyResult {
	/*
	{

    "result": 0,
    "errmsg": ""

	}
	 */

	int result;
	String errmsg;

	
	public String toString() {
		if (0 == result) {
			return String.format("SmsRemoveReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n",
					result,
					errmsg

			);
		} else {
			return String.format("SmsRemoveReplyResult:\n"
							+ "result:%d\n"
							+ "errmsg:%s\n", result, errmsg);
		}
	}
}

