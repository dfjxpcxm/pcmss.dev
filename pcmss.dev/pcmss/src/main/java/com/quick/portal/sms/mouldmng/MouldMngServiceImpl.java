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
package com.quick.portal.sms.mouldmng;

import com.quick.core.base.SysBaseService;
import com.quick.core.base.model.DataStore;
import com.quick.core.base.model.PageBounds;
import com.quick.core.util.common.DateTime;
import com.quick.core.util.common.ReflectUtil;
import com.quick.portal.sms.signmng.SignMngDO;
import com.quick.portal.sms.smsServices.SmsConstants;
import com.quick.portal.sms.smsServices.SmsRemoveReplyResult;
import com.quick.portal.sms.smsServices.SmsSignReplyResult;
import com.quick.portal.sms.smsServices.SmsSignSender;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * sys_mould_info服务实现类
 */
@Transactional
@Service("mouldMngService")
public class MouldMngServiceImpl extends SysBaseService<MouldMngDO> implements IMouldMngService {


    public  SmsTempleSender smsTemple;
    /**
     * 构造函数
     */
    @Autowired
    public MouldMngServiceImpl(IMouldMngDao<MouldMngDO> dao) {
        BaseTable = "sys_mould_info";
        BaseComment = "sys_mould_info";
        PrimaryKey = "mould_id";
        NameKey = "mould_id";

        setDao(dao);
        this.dao = dao;
    }

    private IMouldMngDao<MouldMngDO> dao;


    @Override
    public List<Map<String, Object>> getMouldTypeData() {
        return dao.getMouldTypeData();
    }



    private Class<SmsMngDO> entityClass;


   /**
    *
    * 调用模板接口
    */
    @Override
    public DataStore save(MouldMngDO entity) {
        boolean bool = false;
        //如果编号为空,新增实体对象,否则更新实体对象
        Integer keyVal = entity.getMould_id();
        smsTemple = new SmsTempleSender(SmsConstants.SMS_APPID,SmsConstants.SMS_APPKEY);
        int c = 0;
        if (keyVal == null || keyVal == 0) {
            entity.setMould_state(1);
            entity.setMould_num(0);
            c = dao.insert(entity);
            keyVal = getTplInfoByName(entity.getMould_name());
            //调用创建模板接口
            bool =  sendAddTplInfo(entity,keyVal);
        } else {
            c = dao.update(entity);
            //调用修改模板接口
            bool =  sendModTplInfo(entity,keyVal);
        }
        if (c == 0) {
            return ActionMsg.setError("操作失败");
        }
        if (!bool) {
            return ActionMsg.setError("调用模板接口失败");
        }
        ActionMsg.setValue(entity);
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 调用创建模板接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendAddTplInfo(MouldMngDO entity,Integer id){
        boolean bool = false;
        try{
            SmsTempleReplyResult tplReplyResult = smsTemple.sendTempleInfo("", SmsConstants.INTERNAL_CODE, entity.getMould_content(), entity.getMould_name(),0, entity.getMould_type(),SmsConstants.ADD_TEMPLATE_URL);
            parseSmsTempleReplyResult(tplReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用创建模板接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }
    /**
     * 调用修改模板接口
     * @param entity
     * @param id
     * @return
     */
    public boolean sendModTplInfo(MouldMngDO entity,Integer id){
        boolean bool = false;
        try{
            SmsTempleReplyResult tplReplyResult = smsTemple.sendTempleInfo("", SmsConstants.INTERNAL_CODE, entity.getMould_content(), entity.getMould_name(),entity.getMould_num(), entity.getMould_type(),SmsConstants.MOD_TEMPLATE_URL);
            parseSmsTempleReplyResult(tplReplyResult,id);
            bool = true;
        }catch (Exception e){
            System.out.println("调用修改模板接口失败！"+e.getLocalizedMessage());
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }


    /**
     * 通过模板名称查询模板信息
     */
    public Integer getTplInfoByName(String tplName){
        Integer sid = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("mould_name",tplName);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            sid = Integer.valueOf(mp.get("mould_id").toString());
        }
        return sid;
    }


    /**
     *
     * 解析签名返回内容
     *{
     *     "result": 0,
     *     "errmsg": "",
     *     "data": {
     *         "id": 123,
     *         "international": 0,
     *         "status": 1,
     *         "text": "xxxxx",
     *         "type": 0
     *     }
     * }
     *
     */
    public void parseSmsTempleReplyResult (SmsTempleReplyResult tplReplyResult, int sid){
        int result = tplReplyResult.result;
        MouldMngDO entity  = new MouldMngDO();
        if(result == 0){
            ArrayList<SmsTempleReplyResult.data> datas = tplReplyResult.datas;
            for(SmsTempleReplyResult.data dt : datas){
                entity = new MouldMngDO();
                entity.setMould_num(dt.id);
                entity.setMould_state(dt.status);
                entity.setMould_content(dt.text);
                entity.setMould_type(dt.type);
                entity.setMould_id(sid);
                dao.update(entity);
            }
        }else{
            entity.setMould_state(4);
            entity.setRemarks(tplReplyResult.errmsg);
            entity.setMould_id(sid);
            dao.update(entity);
        }
    }



    /**
     * 调用删除模板接口
     * @param sysid
     * @return
     */
    @Override
    public DataStore delete(String sysid) {
        String snum = getTplInfoByID(sysid);
        dao.delete(sysid);
        ArrayList<String> tplNums = new ArrayList<>();
        tplNums.add(snum);
        //调用删除签名接口
        try{
            SmsRemoveReplyResult tplReplyResult = smsTemple.removeTempleInfo(tplNums, SmsConstants.MOD_TEMPLATE_URL);
        }catch (Exception e){
            System.out.println("调用删除模板接口失败！"+e.getLocalizedMessage());
            return null;
        }
        return ActionMsg.setOk("操作成功");
    }

    /**
     * 通过签名ID查询签名签名编号
     */
    public String getTplInfoByID(String sysid){
        String snum = "";
        Map<String, Object> map = new HashMap<>();
        map.put("mould_id",sysid);
        List<Map<String, Object>> retList = dao.select(map);
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            snum = mp.get("mould_num").toString();
        }
        return snum;
    }

    /**
     * 获取模板编号
     */
    public MouldMngDO getTplInfo(Integer mtype,String content){
        Map<String, Object> map = new HashMap<>();
        map.put("mould_type",mtype);
        map.put("mould_content",content);
        List<Map<String, Object>> retList = dao.select(map);
        MouldMngDO entity = new MouldMngDO();
        if(null !=retList && retList.size()>0){
            Map<String, Object> mp = retList.get(0);
            entity.setMould_id(Integer.valueOf(mp.get("mould_id").toString()));
            entity.setMould_num(Integer.valueOf(mp.get("mould_num").toString()));
        }
        return entity;
    }
}