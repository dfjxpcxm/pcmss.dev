package com.quick.portal.sms.smsServices;

public class SmsPullSendStatusResult implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	public class Data {
		public long success;
		public long bill_number;
		public long request;

	}

	public int result;
	public String errmsg = "";
	private Object data;





}
