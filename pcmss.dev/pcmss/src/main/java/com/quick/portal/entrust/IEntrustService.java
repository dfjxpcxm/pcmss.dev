/**
 * <h3>标题 : potal统一门户-page </h3>
 * <h3>描述 : page服务接口</h3>
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

import java.util.List;
import java.util.Map;

/**
 * page服务接口
 */
public interface IEntrustService extends ISysBaseService<EntrustDO> {
	//List<Map<String,Object>> select(String user_name,String user_id);
	
	int addInfo(List<String> list,String user_id,String to_user_id);
	
	List<Map<String,Object>> selectFrom(String from_user_id,String to_user_id);

	int deleteBy(String from_user_id, String to_user_id);

	List<Map<String,Object>> selectUserMenu(String user_id);

	List<Map<String,Object>> selectNoEntrust();
}