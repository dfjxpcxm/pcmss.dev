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
package com.quick.portal.sms.commonmouldmng;

import com.quick.core.base.SysBaseService;
import com.quick.portal.sms.commonmouldmng.ICommonMouldMngDao;
import com.quick.portal.sms.mouldmng.IMouldMngDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * sys_mould_info服务实现类
 */
@Transactional
@Service("commonMouldMngService")
public class CommonMouldMngServiceImpl extends SysBaseService<CommonMouldMngDO> implements ICommonMouldMngService {


    /**
     * 构造函数
     */
    @Autowired
    public CommonMouldMngServiceImpl(ICommonMouldMngDao<CommonMouldMngDO> dao) {
        BaseTable = "sms_common_mould";
        BaseComment = "sms_common_mould";
        PrimaryKey = "common_mould_id";
        NameKey = "common_mould_id";
        setDao(dao);
        this.dao = dao;
    }

    private ICommonMouldMngDao<CommonMouldMngDO> dao;

}