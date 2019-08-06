/**
 * <h3>标题 : potal统一门户-sys_user </h3>
 * <h3>描述 : sys_user服务实现类</h3>
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
package com.quick.portal.sms.smslogmng;

import com.quick.core.base.SysBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * sys_sms_log服务实现类
 */
@Transactional
@Service("smsLogMngService")
public class SmsLogMngServiceImpl extends SysBaseService<SmsLogMngDO> implements ISmsLogMngService {

    /**
     * 构造函数
     */
    @Autowired
    public SmsLogMngServiceImpl(ISmsLogMngDao<SmsLogMngDO> dao) {
        BaseTable = "sys_sms_log";
        BaseComment = "sys_sms_log";
        PrimaryKey = "sms_log_id";
        NameKey = "sms_log_id";

        setDao(dao);
        this.dao = dao;
    }

    private ISmsLogMngDao<SmsLogMngDO> dao;


    @Override
    public void saveSmsLogInfo(int sms_id, String log_title) {
        SmsLogMngDO entity = new SmsLogMngDO () ;
        entity.setSend_id(sms_id);
        entity.setSms_log_title(log_title);
        dao.saveSmsLogInfo(entity);
    }
}