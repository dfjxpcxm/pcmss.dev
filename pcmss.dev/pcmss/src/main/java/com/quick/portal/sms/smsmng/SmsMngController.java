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
package com.quick.portal.sms.smsmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.JsonDataGrid;
import com.quick.core.util.common.QCookie;
import com.quick.portal.search.infomng.FileUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/smsMng")
public class SmsMngController extends SysBaseController<SmsMngDO> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "smsMngService")
    private ISmsMngService smsMngService;

    @Override
    public ISysBaseService<SmsMngDO> getBaseService() {
        return smsMngService;
    }

    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String edit(ModelMap model) {
        String userId = QCookie.getValue(request, "sbd.user_id");
        model.addAttribute("sms_author", userId);
        return view();
    }




    @RequestMapping(value = "/getSignNameData")
    @ResponseBody
    public Object getSignNameData() throws Exception {
        List<Map<String, Object>> list = smsMngService.getSignNameData();
        return new JsonDataGrid(list.size(), list).toObj();
    }

    @RequestMapping(value = "/getMouldNameData")
    @ResponseBody
    public Object getMouldNameData() throws Exception {
        List<Map<String, Object>> list = smsMngService.getMouldNameData();
        return new JsonDataGrid(list.size(), list).toObj();
    }
    @RequestMapping(value = "/save")
    @ResponseBody
    public DataStore saveAction(SmsMngDO model) {
        String filePath = FileUploadUtils.getImgUploadPath(request);
        model.setFilePath(filePath);
        return save(model);

    }





}