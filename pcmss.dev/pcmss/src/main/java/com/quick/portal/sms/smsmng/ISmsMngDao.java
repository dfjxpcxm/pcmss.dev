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
package com.quick.portal.sms.smsmng;

import com.quick.core.base.ISysBaseDao;
import com.quick.core.base.model.PageBounds;
import com.quick.portal.security.sysconfmng.SysConfMngDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sms_info数据访问接口
 */
public interface ISmsMngDao<SmsMngDO> extends ISysBaseDao<com.quick.portal.sms.smsmng.SmsMngDO> {
    public List<Map<String, Object>> getSignNameData();

    public List<Map<String, Object>> getMouldNameData();

    public List<Map<String, Object>> getSmsSendData(SmsMngDO sms);

    public Map<String, Object> getSmsSendIdData(String sendID);

    int insertPhoneNUmList(List<SmsMngDO> smsMngDO);


    public void insertSmsReplyResult(SmsMngDO sms);


    public void updateInfo(SmsMngDO sms);


    public List<Map<String, Object>> getSendInfoById(Integer id);
}