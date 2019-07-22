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
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.JsonUtil;
import com.quick.core.util.common.QCommon;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsRemoveReplyResult;
import com.quick.portal.sms.smsServices.SmsSignPullerReplyResult;
import com.quick.portal.sms.smsServices.SmsSignReplyResult;
import com.quick.portal.sms.smsServices.SmsSignSender;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import com.quick.portal.web.home.IHomeService;
import com.quick.portal.web.model.DataResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信签名接口类
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mobile/sign")
public class SignApiController extends SysApiController {

    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;





    /**
     *   api https://cloud.tencent.com/document/product/382/6038
     * 添加短信签名
     *  请求参数
     * {
     *     "pic": "xxxxx",
     *     "international": 0,
     *     "remark": "xxxxx",
     *     "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
     *     "text": "xxxxx",
     *     "time": 1457336869
     * }
     *
     * @param international 0表示国内短信，1表示海外短信，默认为0
     * @param remark  签名备注，比如申请原因，使用场景等
     * @param text    签名内容，不带【】，例如：【腾讯科技】这个签名，这里填"腾讯科技"
     * @return
     */
    @RequestMapping(value = "/addSign")
    @ResponseBody
    public Object addSign(int international,String remark,String text)throws Exception{
    /*
请求包体
{
    "pic": "xxxxx",
    "international": 0,
    "remark": "xxxxx",
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "text": "xxxxx",
    "time": 1457336869

}
应答包体
{
    "result": 0,
    "errmsg": "",
    "data": {
        "id": 123,
        "international": 0,
        "status": 1,
        "text": "xxxxx"
    }
}
*/
        SmsSignSender signMng = new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String pic = SmsConstants.PIC_BASE64_PREFIX.concat(SmsConstants.PIC_BASE64_SUFFIX);
        String url = SmsConstants.ADD_SIGN_URL;
        SmsSignReplyResult signReplyResult = signMng.sendSignInfo(pic, international, remark, text,0, url);



        return signReplyResult;
    }

    /**
     * 修改短信签名
     *
     *
     * @param remark  签名备注，比如申请原因，使用场景等
     * @param signId  0待修改的签名对应的签名 ID
     * @param text    签名内容，不带【】，例如：【腾讯科技】这个签名，这里填"腾讯科技"
     * @return
     */
    @RequestMapping(value = "/modSign")
    @ResponseBody
    public Object modSign(String remark,int signId,String text) throws Exception{
        /*
请求包体
{
    "pic": "xxxxx",
    "remark": "xxxxx",
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "sign_id": 123,
    "text": "xxxxx",
    "time": 1457336869

}
应答包体
{
    "result": 0,
    "errmsg": "",
    "data": {
        "id": 123,
        "international": 0,
        "status": 1,
        "text": "xxxxx"
    }
}
*/

        SmsSignSender signMng =  new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String pic = SmsConstants.PIC_BASE64_PREFIX.concat(SmsConstants.PIC_BASE64_SUFFIX);
        String url = SmsConstants.MOD_SIGN_URL;
        SmsSignReplyResult signReplyResult = signMng.sendSignInfo(pic, 0, remark, text, signId,url);
        return signReplyResult;
    }
    
    
    /**
     * 删除短信签名
     * @param signIds  0待修改的签名对应的签名 ID 123,124
     * @return
     */
    @RequestMapping(value = "/delSign")
    @ResponseBody
    public Object delSign(String signIds) throws Exception{
/*
请求包体
{
   "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "sign_id": [
        123,
        124
    ],
    "time": 1457336869
}
应答包体
{
    "result": 0,
    "errmsg": ""
}
*/      if(null == signIds || "".equals(signIds)){
            throw new Exception("signIds " + signIds + " error");
        }
        String [] ids = signIds.split(",");

        ArrayList<Integer> sIds = new ArrayList<>();
        for(int i= 0; i< ids.length;i++){
            sIds.add(Integer.valueOf(ids[i]));
        }

//        ArrayList<String> signId = (ArrayList<String>) Arrays.asList(ids);
        String url = SmsConstants.DEL_SIGN_URL;
        SmsSignSender signMng =  new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsRemoveReplyResult signReplyResult = signMng.removeSignInfo(sIds,url);
        return signReplyResult;
    }
    
    

    /**
     * 短信签名状态查询
     * @param signIds  待修改的签名对应的签名 ID 123,124
     * @return
     */
    @RequestMapping(value = "/getSign")
    @ResponseBody
    public Object getSign(String signIds) throws Exception{

/*
请求包体
{
   "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "sign_id": [
        123,
        124
    ],
    "time": 1457336869
}
应答包体
{
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
*/      if(null == signIds || "".equals(signIds)){
            throw new Exception("signIds " + signIds + " error");
        }
        String [] ids = signIds.split(",");
        ArrayList<Integer> sIds = new ArrayList<>();
        for(int i= 0; i< ids.length;i++){
            sIds.add(Integer.valueOf(ids[i]));
        }
//        ArrayList<Integer> signId = (ArrayList<Integer>) Arrays.asList(Integer.valueOf(ids));
        String url = SmsConstants.GET_SIGN_URL;
        SmsSignSender signMng =  new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsSignPullerReplyResult signReplyResult = signMng.getSignStatusPullerInfo(sIds,url);
        return signReplyResult;
    }
}