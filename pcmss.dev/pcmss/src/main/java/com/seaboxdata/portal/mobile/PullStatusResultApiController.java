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
import com.quick.portal.sms.smsServices.SearchSmsPullCallbackStatusResult;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsPullCallbackStatusReplyResult;
import com.quick.portal.sms.smsServices.SmsStatusPullCallbackResult;
import com.quick.portal.sms.smsServices.SmsStatusPullReplyResult;
import com.quick.portal.sms.smsServices.SmsStatusPuller;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 拉取短信状态接口类
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mobile/pullStatusResult")
public class PullStatusResultApiController extends SysApiController {

    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;



    /**
     *  https://cloud.tencent.com/document/product/382/5977
     *  拉取短信状态 (短信下发状态)
     * 拉取短信状态（下发状态，短信回复等）。
     * 拉取过的内容不会再重复返回，可以理解为消息队列机制
     * @param max 拉取最大条数，最多100
     * @param type 拉取类型，Enum{0: 短信下发状态, 1: 短信回复}
     * @return
     */
    @RequestMapping(value = "/getPullStatusResult")
    @ResponseBody
    public Object getPullStatusResult(int max)throws Exception{
    /*
请求包体
    {
   "max": 10,
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869,
    "type": 1

}
应答包体
{
         "count": 3,
    "data": [],
    "errmsg": "ok",
    "result": 0
}
*/

        if( max >100){
            throw new Exception("max " + max + " error");
        }
        SmsStatusPuller smsStatusPuller = new SmsStatusPuller(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsStatusPullCallbackResult result = smsStatusPuller.pullCallback(max);
        return result;
    }



    /**
     *  https://cloud.tencent.com/document/product/382/5977
     *  拉取短信状态
     * 拉取短信状态（下发状态，短信回复等）。
     * 拉取过的内容不会再重复返回，可以理解为消息队列机制
     * @param max 拉取最大条数，最多100
     * @param type 拉取类型，Enum{0: 短信下发状态, 1: 短信回复}
     * @return
     */
    @RequestMapping(value = "/getReplyStatusResult")
    @ResponseBody
    public Object getReplyStatusResult(int max)throws Exception{
    /*
请求包体
    {
   "max": 10,
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869,
    "type": 1

}
应答包体
{
         "count": 3,
    "data": [],
    "errmsg": "ok",
    "result": 0
}
*/
        if( max >100){
            throw new Exception("max " + max + " error");
        }
        SmsStatusPuller smsStatusPuller = new SmsStatusPuller(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsStatusPullReplyResult result = smsStatusPuller.pullReply(max);
        return result;
    }

}