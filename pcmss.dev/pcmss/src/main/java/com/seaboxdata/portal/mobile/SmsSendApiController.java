/**
 * <h3>标题 : portal统一门户-管理驾驶仓 </h3>
 * <h3>描述 : 应用中的相关配置信息都放在此</h3>
 * <h3>日期 : 2018-04-13</h3>
 * <h3>版权 : Copyright (C) 北京东方金信科技有限公司</h3>
 *
 * <p>
 * @author admin mazong@seaboxdata.com
 * @version <b>v1.0.0</b>
 *
 * <b>修改历史:</b>
 * -------------------------------------------
 * 修改人  修改日期   修改描述
 * -------------------------------------------
 *
 *
 * </p>
 */
package com.seaboxdata.portal.mobile;

import com.quick.core.base.SysApiController;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsMultiSender;
import com.quick.portal.sms.smsServices.SmsMultiSenderResult;
import com.quick.portal.sms.smsServices.SmsRemoveReplyResult;
import com.quick.portal.sms.smsServices.SmsSignReplyResult;
import com.quick.portal.sms.smsServices.SmsSingleSender;
import com.quick.portal.sms.smsServices.SmsSingleSenderResult;
import com.quick.portal.sms.smsServices.SmsTemplePullerReplyResult;
import com.quick.portal.sms.smsServices.SmsTempleReplyResult;
import com.quick.portal.sms.smsServices.SmsTempleSender;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import com.quick.portal.sms.smssystem.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 指定模板单发短信接口类
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mobile/smssend")
public class SmsSendApiController extends SysApiController {

    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;





    /**
     *  https://cloud.tencent.com/document/product/382/5976
     * 指定模板单发短信
     *  请求参数
     * {
     "ext": "",
     "extend": "",
     "params": [
     "验证码",
     "1234",
     "4"
     ],
     "sig": "ecab4881ee80ad3d76bb1da68387428ca752eb885e52621a3129dcf4d9bc4fd4",
     "sign": "腾讯云",
     "tel": {
     "mobile": "13788888888",
     "nationcode": "86"
     },
     "time": 1457336869,
     "tpl_id": 19
     * }
     *
     * 指定模板单发
     * param nationCode 国家码，如 86 为中国
     * param phoneNumber 不带国家码的手机号
     * param templId 信息内容
     * param params 模板参数列表，如模板 {1},{2},{3}，那么需要带三个参数
     * param sign 签名，如果填空，系统会使用默认签名
     * param extend 扩展码，可填空
     * param ext 服务端原样返回的参数，可填空
     * @return
     */
    @RequestMapping(value = "/sendData")
    @ResponseBody
    public Object sendData(String params,String sign,String mobile,int tplId)throws Exception{
    /*
请求包体
{
    "ext": "",
    "extend": "",
    "params": [
        "验证码",
        "1234",
        "4"
    ],
    "sig": "ecab4881ee80ad3d76bb1da68387428ca752eb885e52621a3129dcf4d9bc4fd4",
    "sign": "腾讯云",
    "tel": {
        "mobile": "13788888888",
        "nationcode": "86"
    },
    "time": 1457336869,
    "tpl_id": 19

}
应答包体
{
  "result": 0,
    "errmsg": "OK",
    "ext": "",
    "fee": 1,
    "sid": "xxxxxxx"
}
*/
/*        if(null == params || "".equals(params)){
            SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "模板参数列表为空";
            return signReplyResult;
        }*/
        SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
        if(tplId <1){
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "模板编号为空";
            return signReplyResult;
        }
        if(null == mobile || "".equals(mobile)){
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "手机号为空";
            return signReplyResult;
        }

        ArrayList<String>  parmStr = new ArrayList<>();
        if(null != params && !"".equals(params)){
            String [] parms = params.split(",");
            for(String str:parms) {
                parmStr.add(str);
            }
        }

        SmsSingleSender smsSingleSender = new SmsSingleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String url = SmsConstants.SENDSMS_URL;
        String nationCode = SmsConstants.NATION_CODE;
        smsLogMngService.saveSmsLogInfo(12456,"外部系统调用单发短信接口；签名："+sign+"，模板编号："+tplId+"，参数："+params+",发送到手机号"+mobile);
        SmsSingleSenderResult smsSenderResult = smsSingleSender.sendWithParam(nationCode, mobile, tplId, parmStr,sign, "","",url);

        //微服务调用
//        SmsSingleSenderMicro smsSingleSender = new SmsSingleSenderMicro();
//        String url = SmsConstantsMicro.SENDSMS_URL;
//        String nationCode = SmsConstantsMicro.NATION_CODE;
////        smsLogMngService.saveSmsLogInfo(12456,"外部系统调用单发短信接口；签名："+sign+"，模板编号："+tplId+"，参数："+params+",发送到手机号"+mobile);
//        SmsSingleSenderResultMicro smsSenderResult = smsSingleSender.sendWithParam(nationCode, mobile, tplId, params,sign, "","",url);

        return smsSenderResult;
    }


    /**
     *  https://cloud.tencent.com/document/product/382/5977
     * 给用户群发通知类或营销类短信。
     * 手机号码需全部为国内或者海外手机号码（单次提交不超过200个手机号），也支持单发短信。
     * param nationCode 国家码，如 86 为中国
     * param phoneNumber 不带国家码的手机号
     * param templId 信息内容
     * param params 模板参数列表，如模板 {1},{2},{3}，那么需要带三个参数
     * param sign 签名，如果填空，系统会使用默认签名
     * param extend 扩展码，可填空
     * param ext 服务端原样返回的参数，可填空
     * @return
     */
    @RequestMapping(value = "/smsMutSend")
    @ResponseBody
    public Object smsMutSend(String params,String sign,String mobiles,int tplId)throws Exception{
    /*
请求包体
    {
    "ext": "",
    "extend": "",
    "params": [
        "验证码",
        "1234",
        "4"
    ],
    "sig": "be66bb4aeb54701ed0637d0996a0b75111d5b8eda9b3a71bdc579a3d26f3edfb",
    "sign": "腾讯云",
    "tel": [
        {
            "mobile": "13788888888",
            "nationcode": "86"
        },
        {
            "mobile": "13788888889",
            "nationcode": "86"
        }
    ],
    "time": 1457336869,
    "tpl_id": 19
}
应答包体
{
  "result": 0,
    "errmsg": "OK",
    "ext": "",
    "detail": [
        {
            "errmsg": "OK",
            "fee": 1,
            "mobile": "13788888888",
            "nationcode": "86",
            "result": 0,
            "sid": "xxxxxxx"
        },
        {
            "errmsg": "OK",
            "fee": 1,
            "mobile": "13788888889",
            "nationcode": "86",
            "result": 0,
            "sid": "xxxxxxx"
        }
    ]
}
*/
//        if(null == params || "".equals(params)){
//            SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
//            signReplyResult.result = 9999;
//            signReplyResult.errmsg = "模板参数列表为空";
//            return signReplyResult;
//        }
        SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
        if(tplId <1){

            signReplyResult.result = 9999;
            signReplyResult.errmsg = "模板编号为空";
            return signReplyResult;
        }
        if(params == null){
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "参数为空";
            return signReplyResult;
        }
        if(null == mobiles || "".equals(mobiles)){
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "手机号为空";
            return signReplyResult;
        }
//        ArrayList<String>  paramStr = new ArrayList<>();
//        if(null != params && !"".equals(params)){
//            String [] parms = params.split(",");
//            for(String str:parms) {
//                paramStr.add(str);
//            }
//        }
//
//        String [] tels = mobiles.split(",");
//        ArrayList<String>  telStr = new ArrayList<>();
//        for(String tel:tels) {
//            telStr.add(tel);
//        }
//        SmsMultiSender smsMultiSender = new SmsMultiSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
//        String url = SmsConstants.SENDMULTISMS_URL;
//        String nationCode = SmsConstants.NATION_CODE;
//        smsLogMngService.saveSmsLogInfo(99456,"外部系统调用群发短信接口；签名："+sign+"，模板编号："+tplId+"，参数："+params+",发送到手机号"+mobiles);
//        SmsMultiSenderResult smsSenderResult = smsMultiSender.sendWithParam(nationCode, telStr, tplId, paramStr, sign, "", "", url);
//
//        //微服务调用
        String[] mobilesstr = {};
        if(null != mobilesstr && !"".equals(mobilesstr)){
            mobilesstr = mobiles.split(",");
        }

        SmsSendHttpClient smsSendHttpClient = new SmsSendHttpClient();
        String url = SmsConstantsMicro.SENDMULTISMS_URL;
        if(mobilesstr.length > 200){
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "手机号超过200个，建议用户使用分批次访问！";
            return signReplyResult;
            //手机号大于200转成数组传输,适用于文件方式发送，这里就不做判断了，告知用户不用超过200个手机号
            //return smsSendHttpClient.smsMutSend(params, sign, mobilesstr, tplId, url);
        }else{
            //手机号小于200直接以字符串的当时传输
            return smsSendHttpClient.smsMutSend(params,sign,mobiles,tplId,url);
        }
//        SmsMultiSenderMicro smsMultiSenderMicro = new SmsMultiSenderMicro();
//        String nationCode = SmsConstantsMicro.NATION_CODE;
//        //smsLogMngService.saveSmsLogInfo(99456,"外部系统调用群发短信接口；签名："+sign+"，模板编号："+tplId+"，参数："+params+",发送到手机号"+mobiles);
//        SmsMultiSenderResultMicro smsSenderResult = smsMultiSenderMicro.sendWithParam(nationCode, telStr, tplId, paramStr, sign, "", "", url);
    }


    @RequestMapping(value = "/sendDataForLog")
    @ResponseBody
    public Object sendDataForLog( String sign,String mobile,int tplId,String params)throws Exception{
    /*
请求包体
{
    "ext": "",
    "extend": "",
    "params": [
        "验证码",
        "1234",
        "4"
    ],
    "sig": "ecab4881ee80ad3d76bb1da68387428ca752eb885e52621a3129dcf4d9bc4fd4",
    "sign": "腾讯云",
    "tel": {
        "mobile": "13788888888",
        "nationcode": "86"
    },
    "time": 1457336869,
    "tpl_id": 19

}
应答包体
{
  "result": 0,
    "errmsg": "OK",
    "ext": "",
    "fee": 1,
    "sid": "xxxxxxx"
}
*/
/*        if(null == params || "".equals(params)){
            SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "模板参数列表为空";
            return signReplyResult;
        }*/
        if(tplId <1){
            SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "模板编号为空";
            return signReplyResult;
        }
        if(null == mobile || "".equals(mobile)){
            SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
            signReplyResult.result = 9999;
            signReplyResult.errmsg = "手机号为空";
            return signReplyResult;
        }
        ArrayList<String>  parmStr = new ArrayList<>();
        if(null != params && !"".equals(params)){
            params =java.net.URLDecoder.decode(params,"UTF-8");
            String [] parms = params.split("#");
            for(String str:parms) {
                parmStr.add(str);
            }
        }

        SmsSingleSender smsSingleSender = new SmsSingleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String url = SmsConstants.SENDSMS_URL;
        String nationCode = SmsConstants.NATION_CODE;
        smsLogMngService.saveSmsLogInfo(12456,"外部系统调用单发短信接口；签名："+sign+"，模板编号："+tplId+"，参数："+params+",发送到手机号"+mobile);
        SmsSingleSenderResult smsSenderResult = smsSingleSender.sendWithParam(nationCode, mobile, tplId, parmStr,sign, "","",url);

        //微服务
//        SmsSingleSenderMicro smsSingleSender = new SmsSingleSenderMicro();
//        String url = SmsConstantsMicro.SENDSMS_URL;
//        String nationCode = SmsConstantsMicro.NATION_CODE;
//        //smsLogMngService.saveSmsLogInfo(12456,"外部系统调用单发短信接口；签名："+sign+"，模板编号："+tplId+"，参数："+params+",发送到手机号"+mobile);
//        SmsSingleSenderResultMicro smsSenderResult = smsSingleSender.sendWithParam(nationCode, mobile, tplId, params,sign, "","",url);

        return smsSenderResult;
    }



}