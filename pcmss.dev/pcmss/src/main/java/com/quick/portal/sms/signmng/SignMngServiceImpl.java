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
package com.quick.portal.sms.signmng;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * sys_sign_info服务实现类
 */
@Transactional
@Service("signMngService")
public class SignMngServiceImpl extends SysBaseService<SignMngDO> implements ISignMngService {

    /**
     * 构造函数
     */
    @Autowired
    public SignMngServiceImpl(ISignMngDao<SignMngDO> dao) {
        BaseTable = "sys_sign_info";
        BaseComment = "sys_sign_info";
        PrimaryKey = "sign_id";
        NameKey = "sign_id";

        setDao(dao);
        this.dao = dao;
    }

    private ISignMngDao<SignMngDO> dao;


    @Override
    public List<Map<String, Object>> getSignTypeData() {
        return dao.getSignTypeData();
    }
}