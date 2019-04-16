/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user数据访问接口</h3>
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

import com.quick.core.base.ISysBaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sys_glabal_parm数据访问接口
 */
public interface ISysConfMngDao<SysConfMngDO> extends ISysBaseDao<SysConfMngDO> {

    //查询系统参数表LDAP配置授权策略
    public List<Map<String,Object>> getLimtIpSystemParmInfo();

    //查询系统参数表LDAP配置授权策略
    public List<Map<String,Object>> getLimtLdapSystemParmInfo();

    //查询用户LDAP属性
    public List<Map<String,Object>> getUserLdapProPertyByUserID(int uid);

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
    public void updResStateInfoByID(@Param("ids") String ids);
	
	//多人认证
    List<Map<String, Object>> getUserInfoPwd(HashMap m);

    List<Map<String, Object>> getUserName(HashMap m);

    List<SysConfMngDO> manyPeopleCertification();
}