/**
 * <h3>标题 : potal统一门户-application </h3>
 * <h3>描述 : application数据访问接口</h3>
 * <h3>日期 : 2018-04-13</h3>
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
package com.quick.portal.web.home;

import com.quick.core.base.ISysBaseDao;
import com.quick.portal.application.ApplicationDO;

import java.util.List;
import java.util.Map;

/**
 * application数据访问接口
 */
public interface IHomeDao extends ISysBaseDao<ApplicationDO> {

    /**
     * 查询用户所有应用
     * @param m
     * @return
     */
    List<Map<String, Object>> queryUserApp(Map<String, Object> m);

    /**
     * 查询用户所有应用2
     * @param m
     * @return
     */
    List<Map<String, Object>> queryUserAllApp(Map<String, Object> m);
    /**
     * 查询所有应用
     * @param m
     * @return
     */
    List<Map<String, Object>> queryApp(Map<String, Object> m);

    /**
     * 添加用户桌面
     * @param m
     * @return
     */
    int addDashboard(Map<String, Object> m);
    /**
     * 添加用户桌面应用
     * @param m
     * @return
     */
    int addDashboard_Apps(Map<String, Object> m);

    int countDashboard(Map<String, Object> m);

    /**
     * 更新应用排序
     * @param m
     * @return
     */
    int updateAppSort(Map<String, Object> m);

    /**
     * 删除用户应用
     * @param config_id
     * @return
     */
    int deleteApp(String config_id);
    /**
     * 添加用户应用
     * @param m
     * @return
     */
    int addApp(Map<String, Object> m);

    Map<String,Object> queryAppConfig(Map<String, Object> m);
    /**
     * 查询用户桌面
     * @param m
     * @return
     */
    List<Map<String, Object>> queryDashboard(Map<String, Object> m);
    
    
    int deleteDashboardAppByID(Map<String, Object> m);
    

    /*
     * app端：查询用户所有应用
     */
    List<Map<String, Object>> queryUserAllByApp(Map<String, Object> m);
    
    
    /**
     * app端 查询当前用户未订阅的应用列表
     * @param m
     * @return
     */
    List<Map<String, Object>> queryUnSubscribeByApp(Map<String, Object> m);
    
    
    /**
     * app端 查询当前用户已订阅的应用列表
     * @param m
     * @return
     */
    List<Map<String, Object>> querySubscribedByApp(Map<String, Object> m);
    
    /*
     * 判断重复数据（应用编号、仪表表编号）
     */
    int isExitsAppInfo(Map<String, Object> m);

}