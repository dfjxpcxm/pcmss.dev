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
package com.quick.portal.sms.smsmng;

import com.quick.core.base.SysBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * sys_glabal_parm服务实现类
 */
@Transactional
@Service("smsMngService")
public class SmsMngServiceImpl extends SysBaseService<SmsMngDO> implements ISmsMngService {

    /**
     * 构造函数
     */
    @Autowired
    public SmsMngServiceImpl(ISmsMngDao<SmsMngDO> dao) {
        BaseTable = "sys_sms_info";
        BaseComment = "sys_sms_info";
        PrimaryKey = "sms_id";
        NameKey = "sms_id";

        setDao(dao);
        this.dao = dao;
    }

    private ISmsMngDao<SmsMngDO> dao;


    @Override
    public List<Map<String, Object>> getSignNameData() {
        return dao.getSignNameData();
    }

    @Override
    public List<Map<String, Object>> getMouldNameData() {
        return dao.getMouldNameData();
    }
}