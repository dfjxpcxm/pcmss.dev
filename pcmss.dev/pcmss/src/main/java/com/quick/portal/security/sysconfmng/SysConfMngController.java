/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user请求类</h3>
 * <h3>日期 : 2018-04-13</h3>
 * <h3>版权 : Copyright (C) 北京东方金信科技有限公司</h3>
 *
 * <p>
 *
 * @author 你自己的姓名 mazong@seaboxdata.com
 * @version <b>v1.0.0</b>
 *
 * <b>修改历史:</b>
 * -------------------------------------------
 * 修改人 修改日期 修改描述
 * -------------------------------------------
 *
 *
 * </p>
 */
package com.quick.portal.security.sysconfmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.model.DataStore;
import com.quick.core.util.common.CommonUtils;
import com.quick.core.util.common.QCookie;
import com.quick.portal.userAccessLog.UserAccessLogServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/security/sysconfmng/sysConfMng")
public class SysConfMngController extends SysBaseController<SysConfMngDO> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "sysConfMngService")
    private ISysConfMngService sysConfMngService;

    @Override
    public ISysBaseService<SysConfMngDO> getBaseService() {
        return sysConfMngService;
    }

    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String edit(ModelMap model) {
        return view();
    }


    public void loggerInfoDeleteUserInfo(String id){
        String userName = QCookie.getValue(request, "sbd.user_name");
        String ip = CommonUtils.getIpAddrAdvanced(request);
        String requestResult = "系统操作员:"+userName+"删除用户信息->用户编号:"+id;
        String operateType = "统一门户->用户管理服务->用户删除";
        String operatedUser = "被操作用户编号:"+id;
        String operLog = "用户管理服务日志->删除用户";
        String serviceName = "服务名称:用户管理服务;服务方法名:";
        UserAccessLogServiceUtils.loggerLogInfo(logger,
                userName,operatedUser,operateType,requestResult,operLog,serviceName,ip);
    }

    public void loggerInfoInsertUserInfo(SysConfMngDO model){
        String userName = QCookie.getValue(request, "sbd.user_name");
        String ip = CommonUtils.getIpAddrAdvanced(request);
        String requestResult = "系统操作员:"+userName+"新增用户信息->新增用户名称:"+model.getSys_id();
        String operateType = "统一门户->用户管理服务->用户新增";
        String operatedUser = "被操作用户名称:"+model.getSys_id();
        String operLog = "用户管理服务日志->新增角用户";
        String serviceName = "服务名称:用户管理服务;服务方法名:";
        UserAccessLogServiceUtils.loggerLogInfo(logger,
                userName,operatedUser,operateType,requestResult,operLog,serviceName,ip);

    }

    public void loggerInfoUpdateUserInfo(SysConfMngDO model){
        String userName = QCookie.getValue(request, "sbd.user_name");
        String ip = CommonUtils.getIpAddrAdvanced(request);
        String requestResult = "系统操作员:"+userName+"修改用户信息->把用户编号:"+model.getSys_id()+"的用户名称修改为"+model.getParm_val();
        String operateType = "统一门户->用户管理服务->用户修改";
        String operatedUser = "被操作用户编号:"+model.getSys_id()+":用户名称:"+model.getParm_title();
        String operLog = "用户管理服务日志->修改用户";
        String serviceName = "服务名称:用户管理服务;服务方法名:";
        UserAccessLogServiceUtils.loggerLogInfo(logger,
                userName,operatedUser,operateType,requestResult,operLog,serviceName,ip);
    }
 /**
     * 多人认证 验证账号密码
     */
    @RequestMapping(value = "/peopleAuthor")
    @ResponseBody
    public DataStore peopleAuthor(String user_name, String user_password){
        if(user_name != null ){
            DataStore ds = sysConfMngService.peopleAuthor(user_name,user_password);
            return ds;
        }
        return ActionMsg.setOk("查询失败");
    }

    /**
     * 验证是否开启多人认证
     * @return
     */
    @RequestMapping(value = "/manyPeopleCertification")
    @ResponseBody
    public DataStore manyPeoCer(){

        DataStore ds = sysConfMngService.manyPeopleCertification();
        return ds;

    }

}