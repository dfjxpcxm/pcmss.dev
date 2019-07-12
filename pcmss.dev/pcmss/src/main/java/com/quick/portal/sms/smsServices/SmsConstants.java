package com.quick.portal.sms.smsServices;

public class SmsConstants {
    //添加短信签名
    public static final String ADD_SIGN_URL = "https://yun.tim.qqcom/v5/tlssmssvr/add_sign";
    //修改短信签名
    public static final String MOD_SIGN_URL = "https://yun.tim.qq.com/v5/tlssmssvr/mod_sign";
    //删除短信签名
    public static final String DEL_SIGN_URL = " https://yun.tim.qq.com/v5/tlssmssvr/del_sign";
    //短信签名状态查询
    public static final String GET_SIGN_URL = "https://yun.tim.qq.com/v5/tlssmssvr/get_sign";
    //添加短信模板
    public static final String ADD_TEMPLATE_URL = "https://yun.tim.qq.com/v5/tlssmssvr/add_template";

    //修改短信模板
    public static final String MOD_TEMPLATE_URL = "https://yun.tim.qq.com/v5/tlssmssvr/mod_template";

    //删除短信模板
    public static final String DEL_TEMPLATE_URL = "https://yun.tim.qq.com/v5/tlssmssvr/del_template";

    //短信模板状态查询
    public static final String GET_TEMPLATE_URL = "https://yun.tim.qq.com/v5/tlssmssvr/get_template";

    //指定模板单发短信
    public static final String SENDSMS_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms";

    //指定模板群发短信
    public static final String SENDMULTISMS_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2";

    //回执数据统计
    public static final String PULLCALLBACKSTATUS_URL = "https://yun.tim.qq.com/v5/tlssmssvr/pullcallbackstatus";

    //拉取短信状态
    public static final String PULLSTATUS_URL ="https://yun.tim.qq.com/v5/tlssmssvr/pullstatus";
    //0: 短信下发状态
    public static final int PULL_STATUS_TYPE = 0 ;
    //1: 短信回复
    public static final int REPLY_STATUS_TYPE = 1 ;


    //国内手机区号
    public static final String NATION_CODE = "86";

    //0表示国内短信
    public static final int INTERNAL_CODE = 0;
    //1表示海外短信
    public static final int EXTERNAL_CODE = 1;




    //appid
    public static final int SMS_APPID = 1400224160;
    //appkey
    public static final String SMS_APPKEY = "de43291c7169027322f84c821517182c";
    //base64
    public final static  String PIC_BASE64_PREFIX = "data:image/jpeg;base64,";
    public final static  String PIC_BASE64_SUFFIX = "/9j/4W+ARXhpZgAATU0AKgAAAAgADgEAAAMAAAABC6AAAAEBAAMAAAABD4AAAAECAAMAAAADAAAA";


    public final static String TARGE_UPLOAD_PATH =  "C:/puchenfile";//PropertiesUtil.getPropery("potal.TARGE_UPLOAD_PATH");

    public final static String SRC_UPLOAD_PATH = "/phoneNumber/";
}
