/**
 * <h3>标题 : potal统一门户-InterfaceInfoMaintain</h3>
 * <h3>描述 : InterfaceInfoMaintain请求类</h3>
 * <h3>版权 : Copyright (C) 北京东方金信科技有限公司</h3>
 * 
 * <p>
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
package com.quick.portal.interfaceManage.interfaceInfoMaintain;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * user_department请求类
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/interfaceManage/interfaceInfoMaintain")
public class InterfaceInfoMaintainController extends SysBaseController<InterfaceInfoMaintainDO> {
    
    @Resource(name = "interfaceInfoMaintainService")
    private IInterfaceInfoMaintainService interfaceInfoMaintainService;
    
    @Override
    public ISysBaseService getBaseService(){
        return interfaceInfoMaintainService;
    }
    
    //页面请求
    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }
    @RequestMapping
    public String edit(ModelMap model) {
        return view();
    }
    @RequestMapping
    public String chose(ModelMap model) {
        return view();
    }
}