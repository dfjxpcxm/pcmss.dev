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
package com.quick.portal.sms.addressbook;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsRemoveReplyResult;
import com.quick.portal.sms.smsServices.SmsTempleReplyResult;
import com.quick.portal.sms.smsServices.SmsTempleSender;
import com.quick.portal.sms.smsmng.SmsMngDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * sys_mould_info服务实现类
 */
@Transactional
@Service("addressBookService")
public class AddressBookServiceImpl extends SysBaseService<AddressBookDO> implements IAddressBookService {


    public  SmsTempleSender smsTemple;
    /**
     * 构造函数
     */
    @Autowired
    public AddressBookServiceImpl(IAddressBookDao<AddressBookDO> dao) {
        BaseTable = "sys_mould_info";
        BaseComment = "sys_mould_info";
        PrimaryKey = "mould_id";
        NameKey = "mould_id";

        setDao(dao);
        this.dao = dao;
    }

    private IAddressBookDao<AddressBookDO> dao;
    /**
     * 调用删除模板接口
     * @param sysid
     * @return
     */
    @Override
    public DataStore delete(String sysid) {
        dao.delete(sysid);

        return ActionMsg.setOk("操作成功");
    }
}