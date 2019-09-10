/**
 * <h3>标题 : potal统一门户-section </h3>
 * <h3>描述 : section请求类</h3>
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
package com.quick.portal.newsMessage;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.SysBaseController;
import com.quick.core.base.model.JsonDataGrid;
import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.QCookie;
import com.quick.core.util.common.QRequest;
import org.apache.ibatis.annotations.Param;
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
 * section请求类
 * @author 你自己的姓名
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/newsMessage")
public class NewsMessageController extends SysBaseController<NewsMessageDO> {
    
    @Resource(name = "newsMessageService")
    private INewsMessageService newsMessageService;
    
    @Override
    public ISysBaseService getBaseService(){
        return newsMessageService;
    }
    
    //页面请求
    @RequestMapping
    public String list(ModelMap model) {
        String resource_status = request.getParameter("resource_status");
        model.addAttribute("resource_status", resource_status);
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
    @RequestMapping
    public String mailbox(ModelMap model) {
        return view();
    }
    @RequestMapping
    public String profile(ModelMap model) {
        return view();
    }


    /**
     * 消息状态
     * @param json
     * @return
     */
    @RequestMapping("/messageStatu")
    @ResponseBody
    public Object messageStatu(String json) {
        json = "page";
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

        String depGId = QCookie.getValue(request, "sbd.dep_global_id");
        String level = QCookie.getValue(request, "sbd.dep_level");
        queryMap.put("dep_id",depGId);
        queryMap.put("dep_level",level);
        PageBounds pager = new PageBounds(pageNo, pageSize);

        List<Map<String, Object>> dt;
        if (json.equals("page")) {
            dt = newsMessageService.messageStatu(pager);
            recordCount = pager.getTotal();
        } else {
            dt = newsMessageService.select(queryMap);
            recordCount = dt.size();
        }

        switch (json) {
            case "data": // 全表格[{}]
                str = new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat);
                break;
            case "dataJs":
                String jsName = QRequest.getString(request, "name");
                if (jsName.length() == 0)
                    jsName = getBaseService().getBaseTable();
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
                    storeName = getBaseService().getBaseTable();
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


    /**
     * 重写getList
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public Object getList() throws Exception {
        return getData("page");
    }

    /**
     * 获取数据集合
     *
     * @return
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    public Object getData(String json ) {
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

        String resource_name = QCookie.getValue(request, "resource_name");
//        String resource_status = request.getParameter("resource_status");
//        if(!resource_status.equals("undefined")){
//            queryMap.put("resource_status",resource_status);
//        }
//        queryMap.put("resource_name",resource_name);

        PageBounds pager = new PageBounds(pageNo, pageSize);

        List<Map<String, Object>> dt;
        if (json.equals("page")) {
            dt = getBaseService().select(queryMap, pager);
            recordCount = pager.getTotal();

        } else {
            dt = getBaseService().select(queryMap);
            recordCount = dt.size();
        }

        switch (json) {
            case "data": // 全表格[{}]
                str = new JsonDataGrid(recordCount, dt).toDataJson(dateTimeFormat);
                break;
            case "dataJs":
                String jsName = QRequest.getString(request, "name");
                if (jsName.length() == 0)
                    jsName = getBaseService().getBaseTable();
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
                    storeName = getBaseService().getBaseTable();
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

    @RequestMapping(value = "/getObj", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getObj() throws Exception {
        String resource_status = request.getParameter("resource_status");
        String sysid = QRequest.getString(request, getBaseService()
                .getPrimaryKey());

        if(resource_status.equals("0")){
            NewsMessageDO newsMessageDO = new NewsMessageDO();
            newsMessageDO.setResource_status(1);
            newsMessageDO.setResource_cd(Integer.valueOf(sysid));
            newsMessageService.update(newsMessageDO);
        }

        Map<String, Object> obj = getBaseService().selectMap(sysid);
        return obj;
    }

}