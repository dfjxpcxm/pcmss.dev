/**
 * <h3>标题 : potal统一门户-page </h3>
 * <h3>描述 : page请求类</h3>
 * <h3>日期 : 2018-05-03</h3>
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
package com.quick.portal.entrust;


import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.util.common.QCookie;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * page请求类
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/entrust")
public class EntrustController extends SysBaseController<EntrustDO> {

    @Resource(name = "entrustService")
    private IEntrustService entrustService;

    
    @Override
    public ISysBaseService getBaseService(){
        return entrustService;
    }
    
    //页面请求
    @RequestMapping
    public String list(ModelMap model) {
        String user_id = QCookie.getValue(request, "sbd.user_id");
        model.addAttribute("user_id", user_id);
        return view();
    }

    @RequestMapping
    public String chose(ModelMap model) {
        return view();
    }

    //增加授权信息
    @RequestMapping(value = "/selectUserMenu")
    @ResponseBody
    public Object selectUserMenu() {
        //获取当前用户id
        String user_id = QCookie.getValue(request, "sbd.user_id");
        List<Map<String, Object>> ls = entrustService.selectUserMenu(user_id);
        return ls;
    }

  /*  //增加授权信息
    @RequestMapping(value = "/selectList")
    @ResponseBody
    public Object selectList(ModelMap model) {
        String user_name=request.getParameter("user_name");
        String user_id = QCookie.getValue(request, "sbd.user_id");
        Map<String,Object> paramMap = new HashMap<String, Object>();
        List<Map<String, Object>> ls = entrustService.select(user_name,user_id);
        paramMap.put("total", ls.size());
        paramMap.put("rows", ls);
        return paramMap;
    }*/
   
    //增加授权信息
    @RequestMapping(value = "/entrustAdd",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public int entrustAdd() {
        String to_user_id=request.getParameter("to_user_id");
        String user_id = QCookie.getValue(request, "sbd.user_id");
        String menu_id1=request.getParameter("menu_id");
        List<String> menu_id = Arrays.asList(menu_id1.split(","));
    	 return entrustService.addInfo(menu_id,user_id,to_user_id);
    }
    
  /*//查询授权信息
    @RequestMapping(value = "/entrustSelectFrom")
    @ResponseBody
    public List<Map<String, Object>> entrustSelectFrom(String from_user_id) {
    	 return entrustService.selectFrom(from_user_id);
    }*/
    
  //删除授权信息
    @RequestMapping(value = "/entrustDelete",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public int entrustDelete() {
        String to_user_id=request.getParameter("to_user_id");
        String user_id = QCookie.getValue(request, "sbd.user_id");
    	int i = entrustService.deleteBy(user_id,to_user_id);
    	 return i;
    }

    //删除授权信息
    @RequestMapping(value = "/selectInfo",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object selectInfo() {
        String to_user_id=request.getParameter("to_user_id");
        String user_id = QCookie.getValue(request, "sbd.user_id");
        List<Map<String, Object>>  ls = entrustService.selectFrom(user_id,to_user_id);
        return ls;
    }

    //删除授权信息
    @RequestMapping(value = "/selectNoEntrust",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public Object selectNoEntrust() {
        List<Map<String, Object>>  ls = entrustService.selectNoEntrust();
        return ls;
    }
}
