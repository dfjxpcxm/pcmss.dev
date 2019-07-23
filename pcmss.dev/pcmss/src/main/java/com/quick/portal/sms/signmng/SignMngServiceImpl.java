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
import com.quick.portal.sms.smsServices.SmsSignPullerReplyResult;
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
        smsSign = new SmsSignSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
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
        boolean bool = false;
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer id = entity.getSign_id();
        int c = 0;
        if(id == null || id == 0) {
            //0：已通过, 1：待审核, 2：已拒绝
            entity.setSign_state(1);
            entity.setSign_num(0);
            c = dao.insert(entity);
            id = getSignInfoByName(entity.getSign_name());
            //调用创建签名接口
            bool = sendAddSignInfo(entity, id);
        }else {
            entity.setSign_state(1);
            c = dao.update(entity);
            if(null != entity.getSign_num() && entity.getSign_num()>0){
                //调用修改签名接口
                bool = sendModSignInfo(entity, id);
            }else{
                bool = sendAddSignInfo(entity, id);
            }

        }
        if(c == 0) {
            return ActionMsg.setError("操作失败");
        }
        if(! bool) {
            return ActionMsg.setError("调用签名接口失败");
        }

        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 调用创建签名接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendAddSignInfo(SignMngDO entity,Integer id){
        boolean bool = false;
        try{
            SmsSignReplyResult signReplyResult = smsSign.sendSignInfo("", SmsConstants.INTERNAL_CODE, entity.getApply_causes(), entity.getSign_name(),0, SmsConstants.ADD_SIGN_URL);
            ParseSignReplyResult(signReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用创建签名接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }


    /**
     * 调用修改签名接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendModSignInfo(SignMngDO entity,Integer id){
        boolean bool = false;
        try{
            SmsSignReplyResult signReplyResult = smsSign.sendSignInfo("", SmsConstants.INTERNAL_CODE, entity.getApply_causes(), entity.getSign_name(),entity.getSign_num(), SmsConstants.MOD_SIGN_URL);
            ParseSignReplyResult(signReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用修改签名接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 通过签名名称查询签名信息
     */
    public Integer getSignInfoByName(String signName){
        Integer sid = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sign_name",signName);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sid = Integer.valueOf(mp.get("sign_id").toString());
        }
        return sid;
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
            entity.setSign_state(4);
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
        Integer snum = getSignInfoByID(sysid);
        dao.delete(sysid);
        ArrayList<Integer> signNums = new ArrayList<>();
        signNums.add(snum);
        //调用删除签名接口
        try{
            SmsRemoveReplyResult signReplyResult = smsSign.removeSignInfo(signNums, SmsConstants.MOD_SIGN_URL);
        }catch (Exception e){
            System.out.println("调用删除签名接口失败！"+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return ActionMsg.setOk("操作成功");
    }


    /**
     * 通过签名ID查询签名签名编号
     */
    public Integer getSignInfoByID(String sysid){
        Integer snum = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("sign_id",sysid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            snum = Integer.valueOf(mp.get("sign_num").toString());
        }
        return snum;
    }


    /**
     * 通过签名ID查询签名名称
     */
    public String getSignInfoById(Integer sid){
        String sname = "";
        Map<String, Object> map = new HashMap<>();
        map.put("sign_id",sid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sname = mp.get("sign_name").toString();
        }
        return sname;
    }

    /**
     * 从腾讯云短信服务器获取签名信息同步数据库
     * @return
     */
    @Override
    public DataStore syncSignInfo() {
        //查询拒绝待审核的签名信息
        List<Map<String, Object>> retList = dao.getSignStauteInfo();
        ArrayList<Integer>  signIds = new ArrayList<>();
        if(null != retList && retList.size()>0){
            for(Map<String, Object> mp :retList){
                signIds.add(Integer.valueOf(mp.get("sign_num").toString()));
            }
            //短信签名状态查询
            try{
                SmsSignPullerReplyResult replyRest = smsSign.getSignStatusPullerInfo(signIds,SmsConstants.GET_SIGN_URL);
                ParseReplyResult(replyRest);
            }catch (Exception e){
                System.out.println("短信签名状态查询！"+e.getLocalizedMessage());
                e.printStackTrace();
            }

        }
        return ActionMsg.setOk("同步成功");

    }

    /**
     *
     * 解析签名返回内容
     *
     * {
     *     "result": 0,
     *     "errmsg": "",
     *     "count": 3,
     *     "data": [{
     *         "id": 123,
     *         "international": 0,
     *         "reply": "xxxxx",
     *         "status": 0,
     *         "text": "xxxxx",
     *         "apply_time": "2018-04-29 10:34:55",
     *         "reply_time": "2018-04-29 10:39:55"
     *     }]
     * }
     *
     */
    public void ParseReplyResult (SmsSignPullerReplyResult signReplyResult){
        int result = signReplyResult.result;
        SignMngDO entity = new SignMngDO();
        if(result == 0){
            ArrayList<SmsSignPullerReplyResult.data> datas = signReplyResult.datas;
            for(SmsSignPullerReplyResult.data dt : datas){
                entity = new SignMngDO();
                entity.setSign_num(dt.id);
                entity.setSign_name(dt.text);
                entity.setRemarks(dt.reply);
                entity.setSign_state(dt.status);
                dao.updateReplyResult(entity);
            }
        }
    }
}