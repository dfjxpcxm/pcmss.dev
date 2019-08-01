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
import com.quick.portal.sms.smsServices.SmsMultiSender;
import com.quick.portal.sms.smsServices.SmsMultiSenderResult;
import com.quick.portal.sms.smsServices.SmsPullCallbackStatusReplyResult;
import com.quick.portal.sms.smsServices.SmsPullSendStatus;
import com.quick.portal.sms.smsServices.SmsPullSendStatusResult;
import com.quick.portal.sms.smsServices.SmsStatusPullerResult;
import com.quick.portal.sms.smslogmng.ISmsLogMngService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 回执数据统计接口类
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mobile/smsCallbackStatus")
public class SmsCallbackStatusResultApiController extends SysApiController {

    @Resource(name = "smsLogMngService")
    private ISmsLogMngService smsLogMngService;





    /**
     *  https://cloud.tencent.com/document/product/382/5977
     *  回执数据统计
     * 拉取一段时间短信回执状态（提交成功量，回执量，回执成功量，回执失败量及失败分布）
     * @param stdate 开始时间，yyyymmddhh 需要拉取的起始时间,精确到小时
     * @param eddate 结束时间，yyyymmddhh 需要拉取的截止时间,精确到小时
     * @return
     */
    @RequestMapping(value = "/getSmsCallbackStatusInfo")
    @ResponseBody
    public Object getSmsCallbackStatusInfo(int stdate,int eddate)throws Exception{
    /*
请求包体
    {
    "begin_date": 2016090800,
    "end_date": 2016090823,
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869

}
应答包体
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
        SearchSmsPullCallbackStatusResult smsCallbackRest = new SearchSmsPullCallbackStatusResult(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        String url = SmsConstants.PULLCALLBACKSTATUS_URL;
        SmsPullCallbackStatusReplyResult smsCallbackResult = smsCallbackRest.getCallbackStatusResult(stdate, eddate, url);
        return smsCallbackResult;
    }



    /**
     *  https://yun.tim.qq.com/v5/tlssmssvr/pullsendstatus
     *  发送数据统计
     * 拉取一段时间内的短信发送状态，包括发送量、成功量以及计费条数。
     * @param stdate 开始时间，yyyymmddhh 需要拉取的起始时间,精确到小时
     * @param eddate 结束时间，yyyymmddhh 需要拉取的截止时间,精确到小时
     * @return
     */
    @RequestMapping(value = "/getSmsPullSendStatus")
    @ResponseBody
    public Object getSmsPullSendStatus(int stdate,int eddate)throws Exception{
    /*
请求包体
    {

    "begin_date": 2016090800,
    "end_date": 2016090823,
    "sig": "c13e54f047ed75e821e698730c72d030dc30e5b510b3f8a0fb6fb7605283d7df",
    "time": 1457336869
    }
应答包体
{
      "result": 0,
    "errmsg": "OK",
    "data": {
        "bill_number": 120,
        "request": 101,
        "success": 100
    }
}
*/

        SmsPullSendStatus smsCallbackRest = new SmsPullSendStatus(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        SmsPullSendStatusResult smsCallbackResult = smsCallbackRest.pullSendStatus(stdate, eddate);
        return smsCallbackResult;
    }

}