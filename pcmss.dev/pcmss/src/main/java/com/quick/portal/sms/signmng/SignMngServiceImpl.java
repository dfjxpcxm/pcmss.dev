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
import com.quick.core.util.common.DateTime;
import com.quick.portal.appClass.AppClassDO;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsRemoveReplyResult;
import com.quick.portal.sms.smsServices.SmsSignReplyResult;
import com.quick.portal.sms.smsServices.SmsSignSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * sys_sign_info服务实现类
 */
@Transactional
@Service("signMngService")
public class SignMngServiceImpl extends SysBaseService<SignMngDO> implements ISignMngService {

    public  SmsSignSender smsSign;
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


    /**
     * 保存业务
     * @return
     */
    @Override
    public DataStore save(SignMngDO entity) {
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer id = entity.getSign_id();
        smsSign = new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        int c = 0;
        if(id == null || id == 0) {
            entity.setSign_state(1);
            c = dao.insert(entity);
            Map<String, Object> map = new HashMap<>();
            map.put("sign_name",entity.getSign_name());
            List<Map<String, Object>> retList = dao.select(map);
            if(null !=retList && retList.size()>0){
                Map<String, Object> mp = retList.get(0);
                id = Integer.valueOf(mp.get("sign_id").toString());
            }
            //调用创建签名接口
            try{
                SmsSignReplyResult signReplyResult = smsSign.sendSignInfo("", SmsConstants.INTERNAL_CODE, entity.getApply_causes(), entity.getSign_name(),0, SmsConstants.ADD_SIGN_URL);
                ParseSignReplyResult(signReplyResult,id);
            }catch (Exception e){
                System.out.println("调用创建签名接口失败！"+e.getLocalizedMessage());
                 return null;
            }
        }else {
            c = dao.update(entity);
            //调用修改签名接口失败
            try{
                SmsSignReplyResult signReplyResult = smsSign.sendSignInfo("", SmsConstants.INTERNAL_CODE, entity.getApply_causes(), entity.getSign_name(),entity.getSign_num(), SmsConstants.MOD_SIGN_URL);
                ParseSignReplyResult(signReplyResult,id);
            }catch (Exception e){
                System.out.println("调用修改签名接口失败！"+e.getLocalizedMessage());
                return null;
            }
        }
        if(c == 0)
            return ActionMsg.setError("操作失败");
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }

    /**
     *
     * 解析签名返回内容
     *
     */
    public void ParseSignReplyResult (SmsSignReplyResult signReplyResult,int sid){
        int result = signReplyResult.result;
        SignMngDO entity  = new SignMngDO();
        if(result == 0){
            ArrayList<SmsSignReplyResult.data> datas = signReplyResult.datas;
            for(SmsSignReplyResult.data dt : datas){
                entity = new SignMngDO();
                entity.setSign_num(dt.id);
                entity.setSign_name(dt.text);
                entity.setSign_state(dt.status);
                entity.setSign_id(sid);
                dao.update(entity);
            }
        }else{
            entity.setRemarks(signReplyResult.errmsg);
            entity.setSign_id(sid);
            dao.update(entity);
        }
    }


    @Override
    public List<Map<String, Object>> getSignTypeData() {
        return dao.getSignTypeData();
    }


    /**
     * 调用删除签名接口
     * @param sysid
     * @return
     */
    @Override
    public DataStore delete(String sysid) {
        dao.delete(sysid);
        ArrayList<String> signIds = new ArrayList<>();
        signIds.add(sysid);
        //调用删除签名接口
        try{
            SmsRemoveReplyResult signReplyResult = smsSign.removeSignInfo(signIds, SmsConstants.MOD_SIGN_URL);
        }catch (Exception e){
            System.out.println("调用删除签名接口失败！"+e.getLocalizedMessage());
            return null;
        }
        return ActionMsg.setOk("操作成功");
    }
}