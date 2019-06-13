/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user服务接口</h3>
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
package com.quick.portal.security.sysconfmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.PageBounds;

import java.util.List;
import java.util.Map;


/**
 * sys_global_parm服务接口
 */
public interface ISysConfMngService extends ISysBaseService<SysConfMngDO> {

	//查询系统参数表IP配置授权策略
	public List<Map<String,Object>> getLimtIpSystemParmInfo();

	//查询系统参数表LDAP配置授权策略
	public List<Map<String,Object>> getLimtLdapSystemParmInfo();

	//查询用户LDAP属性
	public String getUserLdapProPertyByUserID(String uid);

	//查询系统参数表对同一权限可以分配的次数的授权策略控制
	public  List<SysConfMngDO> getLimtResLoginSystemParmInfo();

	//查询系统参数表某一个特定权限配置访问次数
	public int getLimtResLoginInfo(Map<String,Object> mp);


	//查询系统参数表某一时间间隔内一个特定权限配置授权策略
	public List<SysConfMngDO> getLimtTimeLoginSystemParmInfo();

	//查询系统参数表某一时间间隔内一个特定权限配置访问次数
	public int getLimtTimeLoginInfo(Map<String,Object> mp);

	//查询系统参数表一个用户在一时间间隔内可以访问权限资源的次数
	public List<SysConfMngDO> getLimtUserTimeLoginSystemParmInfo();

	///查询系统参数表,一个用户在一时间间隔内可以访问权限资源的次数
	public int getLimtUserTimeLogin(Map<String,Object> mp);

	//修改资源ID状态禁用
	public void updResStateInfoByID(String id);

	//获取策略信息
	public List<Map<String,Object>> getResConfName();
	//获取同一权限可以分配的次数

	public List<Map<String,Object>> selectResInfo(Map<String, Object> map, PageBounds pager);

	public List<Map<String,Object>>  getResUserInfo(Map<String, Object> queryMap, PageBounds pager);

	public Map<String, Object> getSysIdResInfo(String sys_id);

	public Map<String, Object> getSysIdResUserInfo(String sys_id);

	public DataStore deleteRes(String sys_id);

	public List<Map<String,Object>> selectUserInfo();

	//多人认证
	public DataStore peopleAuthor(String user_name, String user_password);

	public DataStore manyPeopleCertification();

	DataStore saveRes(SysConfMngDO entity);

	/*
      系统提供短信发送量的预警功能，用户可以设置相应短信的发送量限额，
       超出限额自定提醒给管理员，防止盗刷等事件发生。
    */
	public List<Map<String,Object>> getLimtMsgSystemParmInfo();


	public boolean getUserMsgDataCnt(String sysMsgCnt);
}