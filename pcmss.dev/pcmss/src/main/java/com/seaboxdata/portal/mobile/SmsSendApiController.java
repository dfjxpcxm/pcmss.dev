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
import com.quick.portal.sms.smsServices.SmsRemoveReplyResult;
import com.quick.portal.sms.smsServices.SmsSingleSender;
import com.quick.portal.sms.smsServices.SmsSingleSenderResult;
import com.quick.portal.sms.smsServices.SmsTemplePullerReplyResult;
import com.quick.portal.sms.smsServices.SmsTempleReplyResult;
import com.quick.portal.sms.smsServices.SmsTempleSender;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
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
    @RequestMapping(value = "/sendsms")
    @ResponseBody
    public Object sendsms(String params,String signNm,String mobile,int tplId)throws Exception{
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
        if(null == params || "".equals(params)){
            throw new Exception("params " + params + " error");
        }
        String [] parms = params.split(",");

        ArrayList<String> parmStr = (ArrayList<String>) Arrays.asList(parms);

        SmsSingleSender smsSingleSender = new SmsSingleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String url = SmsConstants.SENDSMS_URL;
        String nationCode = SmsConstants.NATION_CODE;
        SmsSingleSenderResult smsSenderResult = smsSingleSender.sendWithParam(nationCode, mobile, tplId, parmStr,signNm, "","",url);

        return smsSenderResult;
    }

    /**
     * 修改短信模板
     *
     *
     * @param remark 模板备注，比如申请原因，使用场景等
     * @param text 模板内容
     * @param title 模板名称
     * @param tplId 待修改的模板的模板 ID
     * @param type 短信类型，Enum{0：普通短信, 1：营销短信}
     * @return
     */
    @RequestMapping(value = "/modTemplate")
    @ResponseBody
    public Object modTemplate(String remark,int tplId,String text, String title,int type) throws Exception{
        /*
请求包体
{
    "remark": "xxxxx",
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "text": "xxxxx",
    "time": 1457336869,
    "title": "xxxxx",
    "tpl_id": 123,
    "type": 0

}
应答包体
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

        SmsTempleSender templeMng =  new SmsTempleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String url = SmsConstants.MOD_TEMPLATE_URL;
        SmsTempleReplyResult templeReplyResult = templeMng.sendTempleInfo(remark, 0, text, title,tplId, type,url);
        return templeReplyResult;

    }
    
    
    /**
     * 删除短信模板
     * @param tplIds  待删除的模板 ID 123,124
     * @return
     */
    @RequestMapping(value = "/delTemplate")
    @ResponseBody
    public Object delTemplate(String tplIds) throws Exception{
/*
请求包体
{
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869,
    "tpl_id": [
        123,
        124
    ]
}
应答包体
{
    "result": 0,
    "errmsg": ""
}
*/      if(null == tplIds || "".equals(tplIds)){
            throw new Exception("tplIds " + tplIds + " error");
        }
        String [] ids = tplIds.split(",");
        ArrayList<Integer> tplId = new ArrayList<>();
        for(int i=0; i< ids.length;i++){
            tplId.add(Integer.valueOf(ids[i]));
        }
//        ArrayList<String> tplId = (ArrayList<String>) Arrays.asList(ids);
        String url = SmsConstants.DEL_TEMPLATE_URL;
        SmsTempleSender templeMng =  new SmsTempleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsRemoveReplyResult smsTplRemoveReplyResult = templeMng.removeTempleInfo(tplId,url);
        return smsTplRemoveReplyResult;
    }
    
    

    /**
     * 查询申请的短信模板状态
     * @param tplIds  待删除的模板 ID 123,124
     * @return
     */
    @RequestMapping(value = "/getTemplateInfoById")
    @ResponseBody
    public Object getTemplateInfoById(String tplIds) throws Exception{

/*
请求包体
{
  "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
  "time": 1457336869,
  "tpl_id": [123, 124]
}
应答包体
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
*/      if(null == tplIds || "".equals(tplIds)){
            throw new Exception("tplIds " + tplIds + " error");
        }
        String [] ids = tplIds.split(",");
//        ArrayList<String> tplId = (ArrayList<String>) Arrays.asList(ids);
        ArrayList<Integer> tplId = new ArrayList<>();
        for(int i=0; i< ids.length;i++){
            tplId.add(Integer.valueOf(ids[i]));
        }
        String url = SmsConstants.GET_TEMPLATE_URL;
        SmsTempleSender templeMng = new SmsTempleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsTemplePullerReplyResult templePullerReplyResult = templeMng.getTempleStatusPullerInfoByTplId(tplId,url);
        return templePullerReplyResult;
    }


    /**
     * 查询申请的短信模板状态
     * @param  num 一次拉取的条数，最多50
     * @param  stNum 拉取的偏移量，初始为0，如果要多次拉取，需赋值为上一次的 offset 与 max 字段的和
     * @throws Exception
     * @return
     */
    @RequestMapping(value = "/getTempleInfoByPageNo")
    @ResponseBody
    public Object getTempleInfoByPageNo(int num,int stNum) throws Exception{
/*
请求包体
{
  "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
  "time": 1457336869,
  "tpl_id": [123, 124]
}
应答包体
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
*/      String url = SmsConstants.GET_TEMPLATE_URL;
        SmsTempleSender templeMng =  new SmsTempleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsTemplePullerReplyResult templePullerReplyResult = templeMng.getTempleStatusPullerInfoByPageNo(stNum,num,url);
        return templePullerReplyResult;
    }
}