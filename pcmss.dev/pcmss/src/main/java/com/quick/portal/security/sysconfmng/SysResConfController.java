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
import com.quick.core.base.model.JsonDataGrid;
import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.CommonUtils;
import com.quick.core.util.common.QCookie;
import com.quick.core.util.common.QRequest;
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
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * sys_user请求类
 *
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/security/sysconfmng/sysResConf")
public class SysResConfController extends SysBaseController<SysConfMngDO> {

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

    //以下roy编写---start
    @RequestMapping
    public String res(ModelMap model) {
        return view();
    }

    @RequestMapping
    public String restime(ModelMap model) {
        return view();
    }
    //
    @RequestMapping
    public String resuser(ModelMap model) {
        return view();
    }
    @RequestMapping
    public String resedit(ModelMap model) {
        return view();
    }
    //
    @RequestMapping
    public String restimeedit(ModelMap model) {
        return view();
    }
    @RequestMapping
    public String resuseredit(ModelMap model) {
        return view();
    } @RequestMapping
    //委托跳转
    public String entrust(ModelMap model) {
        return view();
    } @RequestMapping
    public String entrustedit(ModelMap model) {
        return view();
    }
    //选择菜单界面
    @RequestMapping
    public String chose(ModelMap model) {
        return view();
    }
    //用户菜单
    @RequestMapping
    public String userchose(ModelMap model) {
        return view();
    }
    //获取策略信息sys_res_conf_name
    @Override
    public Object getList() throws Exception {
        return getList("page");
    }
    //获取策略信息sys_res_conf_name
    public Object getList(String json) {
        String str = "[]";
        if (json == null)
            json = "data";

        int pageSize = QRequest.getInteger(request, "pageSize", 10); // 获取datagrid传来的行数
        // //每页显示条数
        int pageNo = QRequest.getInteger(request, "page", 1); // 获取datagrid传来的页码

        if (json.equals("obj")) {
            pageSize = 1;
        }

        // 表名
        String tableName = "sys_res_conf_name";
        // 主键
        String primaryKey = "sys_res_id";
        // 排序处理
        String fieldOrder = getFieldOrder();

        String whereStr = getFilterCondition(); // 数据必须受限
        String fieldShow = getFieldShow();

        // 日期格式
        String dateTimeFormat = QRequest.getString(request, "dateTimeFormat",
                "yyyy-MM-dd HH:mm"); // 例：dateTimeFormat=yyyy-MM-dd HH:mm

        response.setContentType(QRequest.getResponseType("json")); // 输出JS文件

        // 默认O条数据
        int recordCount = 0;

        Map<String, Object> queryMap = getQueryMap(request, fieldShow,
                tableName, whereStr, fieldOrder);
        PageBounds pager = new PageBounds(pageNo, pageSize);

        List<Map<String, Object>> dt;
        if (json.equals("page")) {
            dt = sysConfMngService.getResConfName();
            recordCount = pager.getTotal();
        } else {
            dt = sysConfMngService.getResConfName();
            recordCount = dt.size();
        }

        switch (json) {
            case "data": // 全表格[{}]
                str = new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat);
                break;
            case "dataJs":
                String jsName = QRequest.getString(request, "name");
                if (jsName.length() == 0)
                    jsName = sysConfMngService.getBaseTable();
                response.setContentType("application/x-javascript; charset=UTF-8"); // 输出JS文件
                str = MessageFormat.format("var _dataStore[\"{0}\"] = {1} ;",
                        jsName, new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat));
                break;
            case "page": // 分页表格{total:0,rows:[]}
                str = new JsonDataGrid(recordCount, dt).toJson(dateTimeFormat);
                break;
            case "tree": // 树下拉[{id:0,text:"",children:[]}]
                // fieldTree:id列名,text列名,parentid列名,开始节点id
                String fieldTree = QRequest.getString(request, "fieldTree");
                str = new JsonDataGrid(recordCount, dt).toTreeJson(fieldTree);
                break;

            case "obj":
                str = new JsonDataGrid(recordCount, dt).toObjJson();
                break;
            case "combo":
                str = new JsonDataGrid(recordCount, dt).toComboJson(fieldShow);
                break;
            case "store": // 数据仓库
                response.setContentType("application/x-javascript; charset=UTF-8"); // 输出JS文件
                String storeName = QRequest.getString(request, "name");
                if (storeName.length() == 0)
                    storeName = sysConfMngService.getBaseTable();
                str = new JsonDataGrid(recordCount, dt).writeDataStoreJs(fieldShow,
                        storeName);
                break;
        }
        try {
            response.getWriter().print(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取策略信息sys_res_conf联表sys_menu
    @RequestMapping(value = "/getListRes")
    @ResponseBody
    public Object getListRes() throws Exception {
        return getListRes("page");
    }
    //获取策略信息sys_res_conf联表sys_menu
    public Object getListRes(String json) throws Exception {
        String str = "[]";
        if (json == null)
            json = "data";

        int pageSize = QRequest.getInteger(request, "pageSize", 10); // 获取datagrid传来的行数
        // //每页显示条数
        int pageNo = QRequest.getInteger(request, "page", 1); // 获取datagrid传来的页码

        if (json.equals("obj")) {
            pageSize = 1;
        }

        // 表名
        String tableName = "sys_res_conf";
        // 主键
        String primaryKey = getPrimaryKey();
        // 排序处理
        String fieldOrder = getFieldOrder();

        String value = request.getParameter("value");


        String whereStr = getFilterCondition(); // 数据必须受限

        String fieldShow = getFieldShow();

        // 日期格式
        String dateTimeFormat = QRequest.getString(request, "dateTimeFormat",
                "yyyy-MM-dd HH:mm"); // 例：dateTimeFormat=yyyy-MM-dd HH:mm

        response.setContentType(QRequest.getResponseType("json")); // 输出JS文件

        // 默认O条数据
        int recordCount = 0;

        Map<String, Object> queryMap = getQueryMap(request, fieldShow,
                tableName, whereStr, fieldOrder);
        PageBounds pager = new PageBounds(pageNo, pageSize);

        List<Map<String, Object>> dt;
        if (json.equals("page")) {
            if (value.equals("resuseredit")){
                dt = sysConfMngService.getResUserInfo(queryMap,pager);
                recordCount = pager.getTotal();
            }else
            {
                dt = sysConfMngService.selectResInfo(queryMap,pager);
                recordCount = pager.getTotal();
            }

        } else {
            if (value.equals("resuseredit")){
                dt = sysConfMngService.getResUserInfo(queryMap,pager);
                recordCount = dt.size();
            }else
            {
                dt = sysConfMngService.selectResInfo(queryMap,pager);
                recordCount = dt.size();
            }

        }

        switch (json) {
            case "data": // 全表格[{}]
                str = new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat);
                break;
            case "dataJs":
                String jsName = QRequest.getString(request, "name");
                if (jsName.length() == 0)
                    jsName = sysConfMngService.getBaseTable();
                response.setContentType("application/x-javascript; charset=UTF-8"); // 输出JS文件
                str = MessageFormat.format("var _dataStore[\"{0}\"] = {1} ;",
                        jsName, new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat));
                break;
            case "page": // 分页表格{total:0,rows:[]}
                str = new JsonDataGrid(recordCount, dt).toJson(dateTimeFormat);
                break;
            case "tree": // 树下拉[{id:0,text:"",children:[]}]
                // fieldTree:id列名,text列名,parentid列名,开始节点id
                String fieldTree = QRequest.getString(request, "fieldTree");
                str = new JsonDataGrid(recordCount, dt).toTreeJson(fieldTree);
                break;

            case "obj":
                str = new JsonDataGrid(recordCount, dt).toObjJson();
                break;
            case "combo":
                str = new JsonDataGrid(recordCount, dt).toComboJson(fieldShow);
                break;
            case "store": // 数据仓库
                response.setContentType("application/x-javascript; charset=UTF-8"); // 输出JS文件
                String storeName = QRequest.getString(request, "name");
                if (storeName.length() == 0)
                    storeName = sysConfMngService.getBaseTable();
                str = new JsonDataGrid(recordCount, dt).writeDataStoreJs(fieldShow,
                        storeName);
                break;
        }
        try {
            response.getWriter().print(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取信息sys_res_conf联表sys_menu 根据ID查询结果
    @RequestMapping(value = "/getObjRes", method = RequestMethod.POST)
    @ResponseBody
    public Object getDataRes() throws Exception {
        String sysid = QRequest.getString(request, getBaseService()
                .getPrimaryKey());
        Map<String, Object> obj = sysConfMngService.getSysIdResInfo(sysid);
        return obj;

    }
    //资源用户信息 sys_user,sys_res_conf,sys_menu
    @RequestMapping(value = "/getObjResUser", method = RequestMethod.POST)
    @ResponseBody
    public Object getObjResUser() throws Exception {
        String sysid = QRequest.getString(request, getBaseService()
                .getPrimaryKey());
        Map<String, Object> obj = sysConfMngService.getSysIdResUserInfo(sysid);
        return obj;

    }

    //保存策略资源
    @RequestMapping(value = "/saveRes")
    @ResponseBody
    public DataStore saveRes(SysConfMngDO entity) {
        return sysConfMngService.saveRes(entity);
    }
    //删除策略资源
    @RequestMapping(value = "/deleteRes")
    @ResponseBody
    public DataStore deleteResAction() {
        String id = rstr(getBaseService().getPrimaryKey());
        return sysConfMngService.deleteRes(id);
    }

    //获取用户数据 用于choose请求
    @RequestMapping(value = "/getUserData")
    @ResponseBody
    public Object  getUserData(){
        return getUserData("page");
    }

    //资源用户数据 sys_user,sys_res_conf
    public Object getUserData(String json){
        String str = "[]";
        if (json == null)
            json = "data";

        int pageSize = QRequest.getInteger(request, "pageSize", 10); // 获取datagrid传来的行数
        // //每页显示条数
        int pageNo = QRequest.getInteger(request, "page", 1); // 获取datagrid传来的页码

        if (json.equals("obj")) {
            pageSize = 1;
        }

        // 表名
        String tableName = getTableName();
        // 主键
        String primaryKey = getPrimaryKey();
        // 排序处理
        String fieldOrder = getFieldOrder();

        String whereStr = getFilterCondition(); // 数据必须受限
        String fieldShow = getFieldShow();

        // 日期格式
        String dateTimeFormat = QRequest.getString(request, "dateTimeFormat",
                "yyyy-MM-dd HH:mm"); // 例：dateTimeFormat=yyyy-MM-dd HH:mm

        response.setContentType(QRequest.getResponseType("json")); // 输出JS文件

        // 默认O条数据
        int recordCount = 0;

        Map<String, Object> queryMap = getQueryMap(request, fieldShow,
                tableName, whereStr, fieldOrder);
        PageBounds pager = new PageBounds(pageNo, pageSize);

        List<Map<String, Object>> dt;
        if (json.equals("page")) {
            dt = sysConfMngService.selectUserInfo();
            recordCount = pager.getTotal();
        } else {
            dt = sysConfMngService.selectUserInfo();
            recordCount = dt.size();
        }

        switch (json) {
            case "data": // 全表格[{}]
                str = new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat);
                break;
            case "dataJs":
                String jsName = QRequest.getString(request, "name");
                if (jsName.length() == 0)
                    jsName = sysConfMngService.getBaseTable();
                response.setContentType("application/x-javascript; charset=UTF-8"); // 输出JS文件
                str = MessageFormat.format("var _dataStore[\"{0}\"] = {1} ;",
                        jsName, new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat));
                break;
            case "page": // 分页表格{total:0,rows:[]}
                str = new JsonDataGrid(recordCount, dt).toJson(dateTimeFormat);
                break;
            case "tree": // 树下拉[{id:0,text:"",children:[]}]
                // fieldTree:id列名,text列名,parentid列名,开始节点id
                String fieldTree = QRequest.getString(request, "fieldTree");
                str = new JsonDataGrid(recordCount, dt).toTreeJson(fieldTree);
                break;

            case "obj":
                str = new JsonDataGrid(recordCount, dt).toObjJson();
                break;
            case "combo":
                str = new JsonDataGrid(recordCount, dt).toComboJson(fieldShow);
                break;
            case "store": // 数据仓库
                response.setContentType("application/x-javascript; charset=UTF-8"); // 输出JS文件
                String storeName = QRequest.getString(request, "name");
                if (storeName.length() == 0)
                    storeName = sysConfMngService.getBaseTable();
                str = new JsonDataGrid(recordCount, dt).writeDataStoreJs(fieldShow,
                        storeName);
                break;
        }
        try {
            response.getWriter().print(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}