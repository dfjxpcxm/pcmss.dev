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
import com.quick.core.base.exception.ExceptionEnumServiceImpl;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.JsonUtil;
import com.quick.core.util.common.QCommon;
import com.quick.portal.newsMessage.INewsMessageService;
import com.quick.portal.newsMessage.NewsMessageDO;
import com.quick.portal.security.authority.metric.PropertiesUtil;
import com.quick.portal.sms.smsServices.SmsSignPullerReplyResult;
import com.quick.portal.sms.smsServices.SmsSignReplyResult;
import com.quick.portal.web.home.IHomeService;
import com.quick.portal.web.model.DataResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门户API接口类
 *
 * @author Administrator
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/mobile/resource")
public class ResourceApiController extends SysApiController {



    @Resource(name = "newsMessageService")
    private INewsMessageService newsMessageService;
    

    
    /**
     * 推送数据资源信息到统一门户系统；
     * 资源目录数据发送变化是主动往统一门户推送发生变化的资源目录数据；
     * i
     * @return
     */
    @RequestMapping(value = "/getResourceInfo",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object getResourceInfo(HttpServletRequest request) throws IOException {
        /*
          {
"result": 0,
"errmsg": "",
"count": 2,
"datas": [
	{
        "resource_id": "300200010006/00001",
        "resource_name": "旅游指南",
        "provider_cd": "350001",
        "check_user": "test01",
        "apply_user": "admin",
        "status": 1,
        "date_time": "2019-07-19 14:29:50"，
        "url": "http://www.baidu.com"

	},
	{
        "resource_id": "300200010007/00002",
        "resource_name": "旅游指南1",
        "provider_cd": "350001",
        "check_user": "test01",
        "apply_user": "admin",
        "status": 1,
        "date_time": "2019-07-19 14:29:50",
         "url": "http://www.baidu.com"
	}
]
}         */

        String data = getHttpClientResourceInfo(request);
        if(null == data || "".equals(data)){
            return getReplyResult(ERROR_CODE,ERROR_MSG);
        }
        JSONObject json = new JSONObject(data);
        Integer result = json.getInt("result");
        if(result >0){
            String errmsg = json.getString("errmsg");
            if(null == errmsg || "".equals(errmsg)){
                errmsg = ERROR_MSG;
            }
            return getReplyResult(ERROR_CODE,errmsg);
        }
        if (true == json.isNull("datas")) {
            return getReplyResult(ERROR_CODE,ERROR_MSG);
        }
        JSONArray datas  = json.getJSONArray("datas");
        NewsMessageDO msgDO = null;
        boolean isExist = false;
        for(int index = 0 ; index< datas.length(); index++){
            msgDO  = new NewsMessageDO ();
            JSONObject reply_json = datas.getJSONObject(index);
            String resid = reply_json.getString("resource_id");
            String resnm = reply_json.getString("resource_name");
            Integer status = reply_json.getInt("status");
            String pcd = reply_json.getString("provider_cd");
            String cuser = reply_json.getString("check_user");
            String auser = reply_json.getString("apply_user");
            String datetime = reply_json.getString("date_time");
            String url = reply_json.getString("url");
            msgDO.setResource_id(resid);
            msgDO.setResource_name(resnm);
            msgDO.setResource_status(status);
            msgDO.setProvider_cd(pcd);
            msgDO.setCheck_user(cuser);
            msgDO.setApply_user(auser);
            msgDO.setDate_time(datetime);
            msgDO.setUrl(getCasUrl(url));
            String desc = getOperDesc(status);
            String operDesc = "用户："+cuser + "，处理资源目录："+ resnm + ",审核状态:"+desc;
            msgDO.setOper_desc(operDesc);
            isExist = this.searchResourceInfo(msgDO);
            if(isExist){
                this.updateResourceInfo(msgDO);
            }else{
                this.insertResourceInfo(msgDO);
            }
        }

        return getReplyResult(0,"");
    }


    public String getCasUrl(String url){
        String casUrl = PropertiesUtil.getPropery("sso.cas.server.loginUrl");
        return casUrl.concat("&service=").concat(url);
//        return casUrl.concat("&service=").concat(QCommon.urlEncode(url));

    }


    public String getOperDesc(Integer status){
        String desc ="";
        switch(status){
            default:
                desc = ExceptionEnumServiceImpl.UNCHECKED.getMessage();
                break;
            case 1:
                desc = ExceptionEnumServiceImpl.UNCHECKED.getMessage();
                break;
            case 2:
                desc = ExceptionEnumServiceImpl.DISABLED.getMessage();
                break;
            case 3:
                desc = ExceptionEnumServiceImpl.NOT_PASS.getMessage();
                break;
            case 4:
                desc = ExceptionEnumServiceImpl.PASSDE.getMessage();
                break;
            case 5:
                desc = ExceptionEnumServiceImpl.PUB_SUC.getMessage();
                break;
        }

        return desc;

    }

    /**
     * 读取资源目录请求JSON数据
     */
    public String getHttpClientResourceInfo(HttpServletRequest request) throws IOException{
        // 读取请求JSON数据
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
        String line = "";
        StringBuilder sbf = new StringBuilder();
        while((line = br.readLine())!=null){
            sbf.append(line);
        }
        String resInfo = sbf.toString();
        return resInfo;
    }

    /**
     *  返回资源目录数据
     */
    public SmsSignReplyResult getReplyResult(Integer code, String msg){
        SmsSignReplyResult signReplyResult = new SmsSignReplyResult();
        signReplyResult.result = code;
        signReplyResult.errmsg = msg;
        return signReplyResult;

    }



    public boolean searchResourceInfo(NewsMessageDO msgDO){
        boolean bool  = newsMessageService.searchResourceInfo(msgDO);
        return bool;
    }


    public void updateResourceInfo(NewsMessageDO msgDO){
        newsMessageService.updateResourceInfo(msgDO);
    }

    public void insertResourceInfo(NewsMessageDO msgDO){
        newsMessageService.insertResourceInfo(msgDO);
    }








    
    


    
    
    private static final String  CANCEL_MSG = "取消成功";
    
    private static final String  SUBSCRIPTION_MSG = "订阅成功";


    private static final String ERROR_MSG = "推送数据资源信息为空";


    private static final int ERROR_CODE = 9999;
	

}