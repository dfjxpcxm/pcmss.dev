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
package com.quick.portal.sms.mouldmng;

import com.quick.core.base.ISysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.PageBounds;

import java.util.List;
import java.util.Map;


/**
 * sys_mould_info服务接口
 */
public interface IMouldMngService extends ISysBaseService<MouldMngDO> {

    public List<Map<String, Object>> getMouldTypeData();


    //获取模板编号
    public MouldMngDO getTplInfo(Integer id);


    List<Map<String, Object>> getMouldTypeContent(Map<String, Object> queryMap);


    public DataStore syncMouldInfo(Integer id);
}