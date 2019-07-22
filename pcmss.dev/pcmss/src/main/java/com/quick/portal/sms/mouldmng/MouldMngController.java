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
package com.quick.portal.sms.mouldmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.JsonDataGrid;
import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.CommonUtils;
import com.quick.core.util.common.QCookie;
import com.quick.core.util.common.QRequest;
import com.quick.portal.sms.smsmng.SmsMngDO;
import com.quick.portal.userAccessLog.UserAccessLogServiceUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/smsMouldMng")
public class MouldMngController extends SysBaseController<MouldMngDO> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "mouldMngService")
    private IMouldMngService mouldMngService;

    @Override
    public ISysBaseService<MouldMngDO> getBaseService() {
        return mouldMngService;
    }

    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String edit(ModelMap model) {
        String userId = QCookie.getValue(request, "sbd.user_id");
        model.addAttribute("mould_author", userId);
        return view();
    }
    @RequestMapping
    public String chose(ModelMap model) {
        return view();
    }




    @RequestMapping(value = "/getMouldTypeData")
    @ResponseBody
    public Object getMouldTypeData() throws Exception {
        List<Map<String, Object>> list = mouldMngService.getMouldTypeData();
        return new JsonDataGrid(list.size(), list).toObj();
    }
    @RequestMapping(value = "/getMouldTypeContent")
    @ResponseBody
    public Object getMouldTypeContent(String mould_type) throws Exception {
        Map<String, Object> queryMap = new HashMap<String,Object>();
        if (!mould_type.equals("") || mould_type != ""){
            queryMap.put("mould_type",mould_type);
        }
        List<Map<String, Object>> list = mouldMngService.getMouldTypeContent(queryMap);
        return new JsonDataGrid(list.size(), list).toObj();
    }



    @RequestMapping(value = "/save")
    @ResponseBody
    public DataStore saveAction(MouldMngDO model) {
        return save(model);

    }




    @RequestMapping(value = "/syncMouldInfo")
    @ResponseBody
    public DataStore syncMouldInfo() {
        DataStore ds = mouldMngService.syncMouldInfo();
        return ds;
    }
}