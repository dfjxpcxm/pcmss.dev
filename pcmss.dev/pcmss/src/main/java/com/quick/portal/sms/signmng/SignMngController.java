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
package com.quick.portal.sms.signmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.JsonDataGrid;
import com.quick.core.util.common.CommonUtils;
import com.quick.core.util.common.QCookie;
import com.quick.portal.userAccessLog.UserAccessLogServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/smsSignMng")
public class SignMngController extends SysBaseController<SignMngDO> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "signMngService")
    private ISignMngService signMngService;

    @Override
    public ISysBaseService<SignMngDO> getBaseService() {
        return signMngService;
    }

    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String edit(ModelMap model) {
        String userId = QCookie.getValue(request, "sbd.user_id");
        model.addAttribute("sign_author", userId);
        return view();
    }



    @RequestMapping(value = "/getSignTypeData")
    @ResponseBody
    public Object getSignTypeData() throws Exception {
        List<Map<String, Object>> list = signMngService.getSignTypeData();
        return new JsonDataGrid(list.size(), list).toObj();

    }



    @RequestMapping(value = "/syncSignInfo")
    @ResponseBody
    public DataStore syncSignInfo() {
        DataStore ds = signMngService.syncSignInfo();
        return ds;
    }

}