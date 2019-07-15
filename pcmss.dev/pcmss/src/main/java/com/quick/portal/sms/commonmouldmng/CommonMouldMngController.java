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
package com.quick.portal.sms.commonmouldmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/commonMouldMng")
public class CommonMouldMngController extends SysBaseController<CommonMouldMngDO> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "commonMouldMngService")
    private ICommonMouldMngService commonMouldMngService;

    @Override
    public ISysBaseService<CommonMouldMngDO> getBaseService() {
        return commonMouldMngService;
    }

    @RequestMapping
    public String list(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String edit(ModelMap model) {
        return view();
    }


}