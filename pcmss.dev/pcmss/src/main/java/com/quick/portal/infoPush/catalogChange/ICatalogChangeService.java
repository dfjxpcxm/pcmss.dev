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
package com.quick.portal.infoPush.catalogChange;

import java.util.List;
import java.util.Map;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.model.DataStore;

/**
 * page服务接口
 */
public interface ICatalogChangeService extends ISysBaseService<CatalogChangeDO> {
	List<Map<String,Object>> select();
	
	int addInfo(List<Map<String,Object>> ls);
}